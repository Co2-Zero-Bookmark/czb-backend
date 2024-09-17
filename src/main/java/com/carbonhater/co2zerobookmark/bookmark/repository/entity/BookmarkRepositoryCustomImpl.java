package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

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
    public List<Bookmark> searchBookmarks(String bookmarkName, String sort, String order, int offset, int limit) {
        QBookmark bookmark = QBookmark.bookmark;

        // 동적 where 절
        BooleanExpression nameCondition = (bookmarkName != null && !bookmarkName.trim().isEmpty())
                ? bookmark.bookmarkName.containsIgnoreCase(bookmarkName)
                : null;

        // 동적 정렬 조건
        OrderSpecifier<?> sortOrder = getSortOrder(bookmark, sort, order);

        return queryFactory.selectFrom(bookmark)
                .where(nameCondition)
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