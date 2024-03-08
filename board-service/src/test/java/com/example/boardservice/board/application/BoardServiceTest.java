package com.example.boardservice.board.application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Map;

import com.example.boardservice.board.application.dto.*;
import com.example.boardservice.board.domain.Board;
import com.example.boardservice.board.domain.BoardRepository;
import com.example.boardservice.board.domain.MemberInfo;
import com.example.boardservice.board.domain.MemberInfoRepository;
import com.example.boardservice.board.exception.BoardException;
import com.example.common.ServiceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@AutoConfigureWireMock(port = 0)
class BoardServiceTest extends ServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    @Nested
    @DisplayName("게시판 페이지 조회 시")
    class ReadPage {

        private final int pageSize = 2;
        private final int totalPostCount = 7;

        @BeforeEach
        void setUp() {
            final MemberInfo memberInfo = new MemberInfo("성하");
            final MemberInfo savedMemberInfo = memberInfoRepository.save(memberInfo);

            for (int i = 1; i <= totalPostCount; i++) {
                final Board board = new Board(savedMemberInfo, "title" + i, "content" + i);
                boardRepository.save(board);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2})
        @DisplayName("페이지 조회에 성공한다.")
        void success(final int pageNumber) {
            // given
            int expectedTotalPageNumber =
                    totalPostCount % pageSize == 0 ? totalPostCount / pageSize : (totalPostCount / pageSize) + 1;

            // when
            final BoardsResponse boardsResponse = boardService.readByPage(PageRequest.of(pageNumber, pageSize));
            final List<BoardListResponse> boardListResponses = boardsResponse.boardListResponses();
            final BoardPageResponse boardPageResponse = boardsResponse.boardPageResponse();

            // then
            final int finalExpectedTotalPageNumber = expectedTotalPageNumber;
            assertSoftly(softly -> {
                softly.assertThat(boardListResponses.get(0).id()).isEqualTo((totalPostCount - ((pageNumber) * pageSize)));
                softly.assertThat(boardPageResponse.currentPageNumber()).isEqualTo(pageNumber + 1);
                softly.assertThat(boardPageResponse.totalPageNumber()).isEqualTo(finalExpectedTotalPageNumber);
            });
        }

        @Test
        @DisplayName("게시글이 존재하지 않을 때 조회에 성공한다.")
        void success_not_exist_post() {
            // given
            int emptyCurrentPageNumber = 1;
            int emptyTotalPageNumber = 0;
            boardRepository.deleteAllInBatch();

            // when
            final BoardsResponse boardsResponse = boardService.readByPage(PageRequest.of(0, pageSize));
            final List<BoardListResponse> boardListResponses = boardsResponse.boardListResponses();
            final BoardPageResponse boardPageResponse = boardsResponse.boardPageResponse();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boardListResponses).isEmpty();
                softly.assertThat(boardPageResponse.currentPageNumber()).isEqualTo(emptyCurrentPageNumber);
                softly.assertThat(boardPageResponse.totalPageNumber()).isEqualTo(emptyTotalPageNumber);
            });
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회 시")
    class ReadDetail {

        private String title = "title1";
        private String content = "content1";
        private String writerNickname = "성하";

        @BeforeEach
        void setUp() {
            final MemberInfo memberInfo = new MemberInfo("성하");

            final Board board = new Board(memberInfo, title, content);
            boardRepository.save(board);
        }

        @Test
        @DisplayName("상세 조회에 성공한다.")
        void success() {
            // when
            final BoardDetailResponse response = boardService.readDetail(1L);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.title()).isEqualTo(title);
                softly.assertThat(response.content()).isEqualTo(content);
                softly.assertThat(response.writerNickname()).isEqualTo(writerNickname);
            });
        }

        @Test
        @DisplayName("없는 게시글 ID로 상세 조회 시 예외가 발생한다.")
        void throws_not_exist_board_id() {
            // given
            final Long notExistBoardId = -1L;

            // when & then
            assertThatThrownBy(() -> boardService.readDetail(notExistBoardId))
                    .isInstanceOf(BoardException.NotFoundBoardException.class)
                    .hasMessage("해당하는 게시글을 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("게시글 작성 시")
    class WriteBoard {

        private String loginId = "sh111";

        @Test
        @DisplayName("작성에 성공한다.")
        void success() throws JsonProcessingException {
            // given
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
            final BoardWriteRequest request = new BoardWriteRequest("newTitle", "newContent");

            // when
            final Long savedBoardId = boardService.writeBoard(loginId, request);

            // then
            assertThat(savedBoardId).isNotNull();
        }

        @Test
        @DisplayName("Login ID가 존재하지 않으면 예외가 발생한다.")
        void throws_not_found_member() throws JsonProcessingException {
            // given
            final String notExistLoginId = "notExistLoginId";
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
            final BoardWriteRequest request = new BoardWriteRequest("newTitle", "newContent");

            // when & then
            assertThatThrownBy(() -> boardService.writeBoard(notExistLoginId, request))
                    .isInstanceOf(FeignException.class)
                    .hasMessageContaining("해당 멤버의 ID가 존재하지 않습니다.");
        }
    }
}
