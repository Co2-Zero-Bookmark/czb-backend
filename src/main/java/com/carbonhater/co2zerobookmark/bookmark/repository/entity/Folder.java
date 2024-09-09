package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.board.repository.entity.BoardFolderRelation;
import jakarta.persistence.*;

import java.util.List;

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
