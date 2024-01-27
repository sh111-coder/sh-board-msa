package com.shboard.shboard.board.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;

import com.shboard.shboard.board.domain.dto.BoardSearchCondition;
import com.shboard.shboard.member.common.RepositoryTest;
import com.shboard.shboard.member.domain.Member;
import com.shboard.shboard.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CustomBoardRepositoryImplTest extends RepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    @Nested
    @DisplayName("조건에 따른 게시판 검색 시 ")
    class SearchByCondition {

        private int memberCount = 3;
        private int totalPostCount = 10;

        @BeforeEach
        void setUp() {
            for (int i = 1 ; i <= memberCount; i++) {
                final Member member = Member.builder()
                        .loginId("sh111")
                        .password("password1!")
                        .nickname("성하" + i)
                        .build();
                memberRepository.save(member);
            }

            for (int i = 1; i <= totalPostCount; i++) {
                Member writer = memberRepository.findById((long) ((i % memberCount) + 1)).get();

                final Board board = new Board(writer, "title" + i, "content" + i);
                boardRepository.save(board);
            }
        }

        @Test
        @DisplayName("아무런 검색 조건, 페이지 조건 없이 검색 시 첫 페이지가 반환된다.")
        void no_condition_first_page() {
            // given
            final BoardSearchCondition condition = new BoardSearchCondition(null, null);
            final Pageable pageable = PageRequest.ofSize(defaultPageSize);
            final int totalPage = totalPostCount % defaultPageSize == 0 ? totalPostCount / defaultPageSize : totalPostCount / defaultPageSize + 1;

            // when
            final Page<Board> boards = boardRepository.searchByCondition(condition, pageable);
            final List<Board> contents = boards.getContent();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boards.isFirst()).isTrue();
                softly.assertThat(boards.getSize()).isEqualTo(defaultPageSize);
                softly.assertThat(boards.getTotalPages()).isEqualTo(totalPage);
                softly.assertThat(boards.getTotalElements()).isEqualTo(totalPostCount);
                softly.assertThat(contents.size()).isEqualTo(defaultPageSize);
                softly.assertThat(contents.get(0).getId()).isEqualTo(totalPostCount);
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        @DisplayName("아무런 검색 조건 없이 페이지 조건으로 검색 시 맞는 페이지가 반환된다.")
        void no_condition_and_page_search_correct_page(int pageNumber) {
            // given
            final BoardSearchCondition condition = new BoardSearchCondition(null, null);
            final Pageable pageable = PageRequest.of(pageNumber - 1, defaultPageSize);

            // when
            final Page<Board> boards = boardRepository.searchByCondition(condition, pageable);
            final List<Board> contents = boards.getContent();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boards.getNumber()).isEqualTo(pageNumber - 1);
                softly.assertThat(boards.getTotalElements()).isEqualTo(totalPostCount);
                softly.assertThat(contents).isNotEmpty();
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"title1", "Title1", "TiTlE1"})
        @DisplayName("제목 검색 조건과 페이지 조건으로 검색 시 맞는 페이지가 반환된다. (제목은 대소문자 구분 X)")
        void search_condition_with_title_and_page_search_correct_page(final String variousCaseSearchTitle) {
            // given
            final BoardSearchCondition condition = new BoardSearchCondition(variousCaseSearchTitle, null);
            final Pageable pageable = PageRequest.ofSize(defaultPageSize);

            // when
            final Page<Board> boards = boardRepository.searchByCondition(condition, pageable);
            final List<Board> contents = boards.getContent();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boards.getNumber()).isEqualTo(0);
                softly.assertThat(boards.getTotalElements()).isEqualTo(2);
                softly.assertThat(contents.size()).isEqualTo(2);
                softly.assertThat(contents)
                        .extracting(Board::getTitle)
                        .allMatch(title -> title.toUpperCase().contains(variousCaseSearchTitle.toUpperCase()));
            });
        }

        @Test
        @DisplayName("작성자 검색 조건과 페이지 조건으로 검색 시 맞는 페이지가 반환된다.")
        void search_condition_with_writer_and_page_search_correct_page() {
            // given
            final String searchWriterNickname = "성하1";
            final BoardSearchCondition condition = new BoardSearchCondition(null, searchWriterNickname);
            final Pageable pageable = PageRequest.ofSize(defaultPageSize);

            // when
            final Page<Board> boards = boardRepository.searchByCondition(condition, pageable);
            final List<Board> contents = boards.getContent();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boards.getNumber()).isEqualTo(0);
                softly.assertThat(boards.getTotalElements()).isEqualTo(3);
                softly.assertThat(contents.size()).isEqualTo(3);
                softly.assertThat(contents)
                        .extracting(Board::getWriterNickname)
                        .allMatch(writerNickname -> writerNickname.equals(searchWriterNickname));
            });
        }

        @Test
        @DisplayName("작성자 & 제목 검색 조건과 페이지 조건으로 검색 시 맞는 페이지가 반환된다.")
        void search_condition_with_writer_and_title_and_page_search_correct_page() {
            // given
            final String searchTitle = "title";
            final String searchWriter = "성하1";
            final BoardSearchCondition condition = new BoardSearchCondition(searchTitle, searchWriter);
            final Pageable pageable = PageRequest.ofSize(defaultPageSize);

            // when
            final Page<Board> boards = boardRepository.searchByCondition(condition, pageable);
            final List<Board> contents = boards.getContent();

            // then
            assertSoftly(softly -> {
                softly.assertThat(boards.getNumber()).isEqualTo(0);
                softly.assertThat(boards.getTotalElements()).isEqualTo(3);
                softly.assertThat(contents.size()).isEqualTo(3);
                softly.assertThat(contents)
                        .extracting(Board::getWriterNickname)
                        .allMatch(writerNickname -> writerNickname.equals(searchWriter));
                softly.assertThat(contents)
                        .extracting(Board::getTitle)
                        .allMatch(title -> title.toUpperCase().contains(searchTitle.toUpperCase()));
            });
        }
    }
}
