package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import jakarta.persistence.*;

@Entity
public class FolderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderHistoryId;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;
    private Long parentFolderId;
    private Long tagId;
    private String folderName;
    private Long userId;

}
