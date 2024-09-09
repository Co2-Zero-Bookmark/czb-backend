package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import jakarta.persistence.*;
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id")
    private Folder folder;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "tag")
    private Tag tag;

    private String folderName;

}
