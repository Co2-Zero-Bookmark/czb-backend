package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByDeletedYn(char deletedYn);
}
