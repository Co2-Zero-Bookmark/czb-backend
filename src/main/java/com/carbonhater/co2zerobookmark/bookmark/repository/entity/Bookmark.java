package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    private String bookmarkName;
    private String bookmarkUrl;

    private Date lastVisitedAt;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
