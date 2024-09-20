package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {
    List<Bookmark> searchBookmarks(String bookmarkName,
                                   String sort,
                                   String order,
                                   int offset,
                                   int limit);
}
