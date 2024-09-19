package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>
                                             {
    List<Bookmark> findByDeletedYn(char deletedYn);
}
