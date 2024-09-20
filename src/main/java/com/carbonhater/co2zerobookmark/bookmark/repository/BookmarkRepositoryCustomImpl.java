package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.QBookmark;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> searchBookmarks(String bookmarkName, Long userId, String sort, String order, int offset, int limit) {
        QBookmark bookmark = QBookmark.bookmark;

        // 동적 where 절
        BooleanExpression nameCondition = (bookmarkName != null && !bookmarkName.trim().isEmpty())
                ? bookmark.bookmarkName.containsIgnoreCase(bookmarkName)
                : null;
        BooleanExpression userCondition = (userId != null)
                ? bookmark.userId.eq(userId)
                : null;

        BooleanExpression deletedCondition = bookmark.deletedYn.eq('N'); // 삭제되지 않은 북마크만

        // 동적 정렬 조건
        OrderSpecifier<?> sortOrder = getSortOrder(bookmark, sort, order);

        return queryFactory.selectFrom(bookmark)
                .where(deletedCondition, nameCondition, userCondition) // 삭제 조건 및 이름 조건을 모두 추가
                .orderBy(sortOrder)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private OrderSpecifier<?> getSortOrder(QBookmark bookmark, String sort, String order) {
        Order direction = order.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

        if ("lastVisitedAt".equalsIgnoreCase(sort)) {
            return new OrderSpecifier<>(direction, bookmark.lastVisitedAt);
        }
        return new OrderSpecifier<>(direction, bookmark.createdAt);
    }
}