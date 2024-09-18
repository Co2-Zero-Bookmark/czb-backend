package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.deletedYn = 'N' and t.tagId = :id")
    Optional<Tag> findActiveById(Long id);

    @Query("select t from Tag t where t.deletedYn = 'N'")
    List<Tag> findActiveAll();
}