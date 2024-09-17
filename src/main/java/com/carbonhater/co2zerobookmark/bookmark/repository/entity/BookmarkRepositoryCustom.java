package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import java.util.List;

public interface BookmarkRepositoryCustom {
    List<Bookmark> searchBookmarks(String bookmarkName,
                                   String sort,
                                   String order,
                                   int offset,
                                   int limit);
}
