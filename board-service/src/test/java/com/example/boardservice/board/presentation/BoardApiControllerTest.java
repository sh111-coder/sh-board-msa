package com.example.boardservice.board.presentation;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.example.boardservice.board.application.dto.BoardListResponse;
import com.example.boardservice.board.application.dto.BoardPageResponse;
import com.example.boardservice.board.application.dto.BoardWriteRequest;
import com.example.boardservice.board.application.dto.BoardsResponse;
import com.example.boardservice.board.domain.Board;
import com.example.boardservice.board.domain.BoardRepository;
import com.example.boardservice.board.domain.MemberInfo;
import com.example.boardservice.board.domain.MemberInfoRepository;
import com.example.common.AcceptanceTest;
import com.example.shboardcommon.global.auth.AuthException;
import com.example.shboardcommon.global.auth.AuthMemberArgumentResolver;
import com.example.shboardcommon.global.auth.AuthMemberId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class BoardApiControllerTest extends AcceptanceTest {

    private static final String LOGIN_ID = "sh111";
    private static final String NICKNAME = "성하";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthMemberArgumentResolver authMemberArgumentResolver;

    private String sessionId;

    @Nested
    @DisplayName("게시글 작성 시")
    class WriteBoard {

        @Test
        @DisplayName("쿠키에 세션이 존재하지 않으면 작성에 실패한다.")
        void fail_not_exist_session_in_cookie() throws Exception {
            // given
            mockArgumentResolverFail();

            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);

            // when
            final ExtractableResponse<Response> response = writeBoardNotExistSessionRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                softly.assertThat(response.body().asString()).contains("인증되지 않은 사용자의 접근입니다.");
            });
        }

        @Test
        @DisplayName("쿠키에 해당하는 세션이 존재하지 않으면 작성에 실패한다.")
        void fail_not_found_session() throws Exception {
            // given
            mockArgumentResolverFail();

            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);
            final String notExistSessionId = "notExistSessionId";
            final String encodedNotExistSessionId = new String(Base64.getEncoder().encode(notExistSessionId.getBytes()));

            // when
            final ExtractableResponse<Response> response = writeBoardRequest(request, encodedNotExistSessionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                softly.assertThat(response.body().asString()).contains("인증되지 않은 사용자의 접근입니다.");
            });
        }

        @Test
        @DisplayName("게시글 작성에 성공한다.")
        void success() throws Exception {
            // given
            mockArgumentResolverSuccess(LOGIN_ID);
            mockFindMemberOpenFeignSuccess(LOGIN_ID);

            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);

            // when
            final ExtractableResponse<Response> response = writeBoardRequest(request, sessionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(response.header("Location")).contains("/boards/");
            });
        }

        @Test
        @DisplayName("로그인 ID에 해당하는 멤버가 존재하지 않으면 게시글 작성에 실패한다.")
        void fail_not_found_login_id_member() throws Exception {
            // given
            final String notExistLoginId = "notExistLoginId";
            mockArgumentResolverSuccess(notExistLoginId);
            mockFindMemberOpenFeignFail(notExistLoginId);

            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);

            // when
            final ExtractableResponse<Response> response = writeBoardRequest(request, sessionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().asString()).contains("해당 멤버의 ID가 존재하지 않습니다.");
            });
        }
    }

    @Nested
    @DisplayName("페이지별 게시글 조회 시")
    class ReadByPage {

        private final int pageSize = 2;
        private final int totalPostCount = 7;

        @Test
        @DisplayName("게시글이 존재하지 않을 시 조회에 성공한다.")
        void success_not_exist_post() {
            // given
            final int pageToRead = 1;
            final int expectedTotalPageNumber = 0;
            final int expectedCurrentPageNumber = 1;

            // when
            sessionId = new String(Base64.getEncoder().encode(LOGIN_ID.getBytes(StandardCharsets.UTF_8)));
            final ExtractableResponse<Response> response = readByPageRequest(pageToRead, pageSize, sessionId);
            final BoardsResponse boardsResponse = response.as(BoardsResponse.class);
            final List<BoardListResponse> boardListResponses = boardsResponse.boardListResponses();
            final BoardPageResponse boardPageResponse = boardsResponse.boardPageResponse();

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(boardListResponses).isEmpty();
                softly.assertThat(boardPageResponse.totalPageNumber()).isEqualTo(expectedTotalPageNumber);
                softly.assertThat(boardPageResponse.currentPageNumber()).isEqualTo(expectedCurrentPageNumber);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        @DisplayName("게시글이 존재할 때 조회에 성공한다.")
        void success_exist_post(final int pageToRead) {
            // given
            final MemberInfo memberInfo = new MemberInfo(NICKNAME);
            final MemberInfo savedMemberInfo = memberInfoRepository.save(memberInfo);
            for (int i = 1; i <= totalPostCount; i++) {
                final Board board = new Board(savedMemberInfo, "title" + i, "content" + i);
                boardRepository.save(board);
            }

            int expectedTotalPageNumber;
            if (totalPostCount % pageSize == 0) {
                expectedTotalPageNumber = totalPostCount / pageSize;
            } else {
                expectedTotalPageNumber = (totalPostCount / pageSize) + 1;
            }

            // when
            sessionId = new String(Base64.getEncoder().encode(LOGIN_ID.getBytes(StandardCharsets.UTF_8)));
            final ExtractableResponse<Response> response = readByPageRequest(pageToRead - 1, pageSize, sessionId);
            final BoardsResponse boardsResponse = response.as(BoardsResponse.class);
            final List<BoardListResponse> boardListResponses = boardsResponse.boardListResponses();
            final BoardPageResponse boardPageResponse = boardsResponse.boardPageResponse();

            // then
            final int finalExpectedTotalPageNumber = expectedTotalPageNumber;
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(boardListResponses.get(0).id()).isEqualTo((totalPostCount - ((pageToRead - 1) * pageSize)));
                softly.assertThat(boardPageResponse.currentPageNumber()).isEqualTo(pageToRead);
                softly.assertThat(boardPageResponse.totalPageNumber()).isEqualTo(finalExpectedTotalPageNumber);
            });
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회 시")
    class ReadDetail {

        @Test
        @DisplayName("게시글 상세 조회에 성공한다.")
        void success() throws Exception {
            // given
            mockArgumentResolverSuccess(LOGIN_ID);
            mockFindMemberOpenFeignSuccess(LOGIN_ID);

            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);
            sessionId = new String(Base64.getEncoder().encode(LOGIN_ID.getBytes(StandardCharsets.UTF_8)));
            writeBoardRequest(request, sessionId);
            final long boardId = 1L;

            // when
            final ExtractableResponse<Response> response = readDetailRequest(boardId, sessionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(response.jsonPath().getLong("id")).isEqualTo(boardId);
                softly.assertThat(response.jsonPath().getString("title")).isEqualTo(title);
                softly.assertThat(response.jsonPath().getString("content")).isEqualTo(content);
                softly.assertThat(response.jsonPath().getString("writerNickname")).isEqualTo(NICKNAME);
            });
        }

        @Test
        @DisplayName("존재하지 않는 게시글 ID로 조회 시 조회에 실패한다.")
        void fail_not_found_board_id() {
            // given
            final String title = "title1";
            final String content = "content1";
            final BoardWriteRequest request = new BoardWriteRequest(title, content);
            sessionId = new String(Base64.getEncoder().encode(LOGIN_ID.getBytes(StandardCharsets.UTF_8)));
            writeBoardRequest(request, sessionId);
            final long notExistBoardId = -1L;

            // when
            final ExtractableResponse<Response> response = readDetailRequest(notExistBoardId, sessionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().asString()).contains("해당하는 게시글을 찾을 수 없습니다.");
            });
        }
    }

    private void mockFindMemberOpenFeignSuccess(final String loginId) throws JsonProcessingException {
        final String expectedResponse = objectMapper.writeValueAsString(
                Map.of(
                        "memberId", 1,
                        "nickname", "성하"
                )
        );

        stubFor(get(urlEqualTo("/api/members?loginId=" + loginId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedResponse))
        );
    }

    private void mockFindMemberOpenFeignFail(final String notExistLoginId) throws JsonProcessingException {
        final String expectedResponse = objectMapper.writeValueAsString(
                Map.of(
                        "errorMessage", "해당 멤버의 ID가 존재하지 않습니다."
                )
        );

        stubFor(get(urlEqualTo("/api/members?loginId=" + notExistLoginId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(expectedResponse))
        );
    }

    private void mockArgumentResolverFail() throws Exception {
        given(authMemberArgumentResolver.supportsParameter(any()))
                .willReturn(true);
        given(authMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willThrow(new AuthException.FailAuthenticationMemberException());
    }

    private void mockArgumentResolverSuccess(final String memberId) throws Exception {
        given(authMemberArgumentResolver.supportsParameter(any()))
                .willReturn(true);
        given(authMemberArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new AuthMemberId(memberId));
    }

    private ExtractableResponse<Response> writeBoardRequest(final BoardWriteRequest request, final String sessionId) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie("JSESSIONID", sessionId)
                .when().log().all()
                .post("/api/boards")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> writeBoardNotExistSessionRequest(final BoardWriteRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/boards")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> readByPageRequest(final int page, final int size, final String sessionId) {
        return RestAssured.given().log().all()
                .param("page", page)
                .param("size", size)
                .sessionId(sessionId)
                .when().log().all()
                .get("/api/boards")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> readByPageNotExistSessionRequest(final int page, final int size) {
        return RestAssured.given().log().all()
                .param("page", page)
                .param("size", size)
                .when().log().all()
                .get("/api/boards")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> readDetailRequest(final Long boardId, final String sessionId) {
        return RestAssured.given().log().all()
                .sessionId(sessionId)
                .when().log().all()
                .get("/api/boards/" + boardId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> readDetailNotExistSessionRequest(final Long boardId) {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/boards/" + boardId)
                .then().log().all()
                .extract();
    }
}
