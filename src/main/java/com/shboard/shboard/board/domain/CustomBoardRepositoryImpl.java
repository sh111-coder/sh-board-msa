package com.shboard.shboard.board.domain;

import static com.shboard.shboard.board.domain.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shboard.shboard.board.domain.dto.BoardSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;

    public CustomBoardRepositoryImpl(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> searchByCondition(final BoardSearchCondition condition, final Pageable pageable) {
        final List<Board> findBoards = queryFactory
                .selectFrom(board)
                .where(titleContains(condition.title()), writerEq(condition.writer()))
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(titleContains(condition.title()), writerEq(condition.writer()));

        return PageableExecutionUtils.getPage(findBoards, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleContains(final String title) {
        return hasText(title) ? board.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression writerEq(final String writer) {
        return hasText(writer) ? board.writer.nickname.nickname.eq(writer) : null;
    }
}
