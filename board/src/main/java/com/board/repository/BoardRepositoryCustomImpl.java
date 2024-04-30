package com.board.repository;

import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import com.board.entity.QBoard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 현재 날짜로부터 이전날짜를 구해주는 메소드
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); // 현재 날짜, 시간

        if (StringUtils.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if (StringUtils.equals("1d", searchDateType))
            dateTime = dateTime.minusDays(1); // 1일 전
        else if (StringUtils.equals("1w", searchDateType))
            dateTime = dateTime.minusWeeks(1); // 1주일 전
        else if (StringUtils.equals("1m", searchDateType))
            dateTime = dateTime.minusMonths(1); // 1개월 전
        else if (StringUtils.equals("6m", searchDateType))
            dateTime = dateTime.minusMonths(6); // 6개월 전

        return QBoard.board.regDate.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QBoard.board.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("content", searchBy)) {
            return QBoard.board.content.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("writer", searchBy)) {
            return QBoard.board.member.name.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Board> getAdminItemPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        // pageable 객체: 한 페이지의 몇 개의 게시물을 보여줄지, 시작 페이지 번호에 대한 정보를 가지고 있다.
        return new PageImpl<>(content, pageable, total);
    }
}
