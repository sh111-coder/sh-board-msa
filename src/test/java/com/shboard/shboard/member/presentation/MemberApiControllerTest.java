package com.shboard.shboard.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.shboard.shboard.member.application.dto.MemberLoginRequest;
import com.shboard.shboard.member.application.dto.MemberRegisterRequest;
import com.shboard.shboard.member.common.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberApiControllerTest extends AcceptanceTest {

    @Nested
    @DisplayName("회원 가입 시")
    class Register {

        final String loginId = "sh111";
        final String password = "password1!";
        final String nickname = "seongha";

        @Test
        @DisplayName("회원 가입에 성공한다.")
        void success() {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(loginId, password, nickname);

            // when
            final ExtractableResponse<Response> response = registerRequest(request);

            // then
            assertThat(response.header("Location")).contains("/members/");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "s", "sh", "sh1", "1234567890123"})
        @DisplayName("4자 이상 12자 이하가 아닌 회원 ID로 회원 가입 시 실패한다.")
        void fail_wrongLengthMemberId(final String wrongLengthMemberId) {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(wrongLengthMemberId, password, nickname);

            // when
            final ExtractableResponse<Response> response = registerRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().jsonPath().getString("errorMessage")).isEqualTo("입력한 회원 ID는 4자 이상 12자 이하여야합니다.");
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "A0!", "1111", "AAaa", "!!@@", "11AA", "11!@", "AA!@"})
        @DisplayName("4자 이상 숫자, 영어, 특수문자 조합이 아닌 비밀번호로 회원 가입 시 실패한다.")
        void fail_wrongPatternPassword(final String wrongPatternPassword) {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(loginId, wrongPatternPassword, nickname);

            // when
            final ExtractableResponse<Response> response = registerRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().jsonPath().getString("errorMessage")).isEqualTo("입력한 비밀번호는 4자 이상 숫자, 영어, 특수문자 조합이어야합니다.");
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "S", "영일이삼사오육칠팔구십1"})
        @DisplayName("2자 이상 10자 이하인 닉네임으로 회원 가입 시 실패한다.")
        void fail_wrongLengthNickname(final String wrongLengthNickname) {
            // given
            final MemberRegisterRequest request = new MemberRegisterRequest(loginId, password, wrongLengthNickname);

            // when
            final ExtractableResponse<Response> response = registerRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().jsonPath().getString("errorMessage")).isEqualTo("입력한 회원 닉네임은 2자 이상 10자 이하여야합니다.");
            });
        }
    }

    @Nested
    @DisplayName("로그인 시")
    class Login {

        final String loginId = "sh111";
        final String password = "password1!";
        final String nickname = "seongha";

        @BeforeEach
        void setUp() {
            final MemberRegisterRequest request = new MemberRegisterRequest(loginId, password, nickname);
            registerRequest(request);
        }

        @Test
        @DisplayName("로그인에 성공한다.")
        void success() {
            // given
            final String loginId = "sh111";
            final String password = "password1!";
            final MemberLoginRequest request = new MemberLoginRequest(loginId, password);

            // when
            final ExtractableResponse<Response> response = loginRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(response.cookie("JSESSIONID")).isNotNull();
            });
        }

        @ParameterizedTest
        @CsvSource(value = {"wrongId:password1!", "sh111:wrongPassword"}, delimiter = ':')
        @DisplayName("아이디나 비밀번호가 일치하지 않으면 로그인에 실패한다.")
        void fail_wrongIdOrPassword(final String loginId, final String password) {
            // given
            final MemberLoginRequest request = new MemberLoginRequest(loginId, password);

            // when
            final ExtractableResponse<Response> response = loginRequest(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().asString()).contains("잘못된 회원 정보를 입력하여 로그인에 실패했습니다.");
                softly.assertThat(response.cookie("JSESSIONID")).isNull();
            });
        }

        private ExtractableResponse<Response> loginRequest(final MemberLoginRequest request) {
            return RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().log().all()
                    .post("/api/members/login")
                    .then().log().all()
                    .extract();
        }
    }

    private ExtractableResponse<Response> registerRequest(final MemberRegisterRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/members/register")
                .then().log().all()
                .extract();
    }
}
