package com.carbonhater.co2zerobookmark.bookmark.repository;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("select f from Folder f where f.deletedYn = 'N' and f.folderId = :id")
    Optional<Folder> findActiveById(Long id);

    @Query("select f from Folder f where f.deletedYn = 'N' and f.userId = :userId and f.folder is null")
    List<Folder> findByUserIdAndFolderIsNull(Long userId);

    @Query("select f from Folder f where f.deletedYn = 'N' and f.folder = :folder")
    List<Folder> findActiveByFolder(Folder folder);
}
