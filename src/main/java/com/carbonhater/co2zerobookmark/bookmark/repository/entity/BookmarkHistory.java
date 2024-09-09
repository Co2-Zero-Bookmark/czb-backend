package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class BookmarkHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkHistoryId;

    @ManyToOne
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;
    private Long folderId;
    private String bookmarkName;
    private String bookmarkUrl;
    private Date lastVisitedDatetime;

}
