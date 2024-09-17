package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.BookmarkHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkHistoryRepository extends JpaRepository<BookmarkHistory, Long> {
}
