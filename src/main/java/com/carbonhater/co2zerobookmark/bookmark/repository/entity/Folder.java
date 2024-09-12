package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Folder extends BaseEntity {

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
