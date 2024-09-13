package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>
                                            , BookmarkRepositoryCustom {
    List<Bookmark> findByDeletedYn(char deletedYn);
}
