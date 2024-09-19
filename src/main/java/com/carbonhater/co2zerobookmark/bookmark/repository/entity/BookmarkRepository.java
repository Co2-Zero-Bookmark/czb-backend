package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>
                                            , BookmarkRepositoryCustom {
    List<Bookmark> findByDeletedYn(char deletedYn);


    @Query("select b from Bookmark b where b.deletedYn = 'N' and b.folder = :folder")
    List<Bookmark> findActiveByFolder(Folder folder);
}
