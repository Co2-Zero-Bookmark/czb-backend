package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class FolderHistory extends BaseEntity {
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


    @Builder
    public FolderHistory(Long folderHistoryId, Folder folder, LocalDateTime now) {
        this.folderHistoryId = folderHistoryId;
        this.folder = folder;
        this.parentFolderId = Objects.isNull(folder.getFolder()) ? null : folder.getFolder().getFolderId();
        this.tagId = Objects.isNull(folder.getTag()) ? null : folder.getTag().getTagId();
        this.folderName = folder.getFolderName();
        this.userId = folder.getUserId();
        //TODO Auditing
        setDeletedYn(folder.getDeletedYn());
        setCreatedAt(now);
        setCreatedId(folder.getUserId());
        setModifiedAt(now);
        setModifiedId(folder.getUserId());
    }


    public static FolderHistory create(Folder folder, LocalDateTime now) {
        return FolderHistory.builder()
                .folder(folder)
                .now(now)
                .build();
    }
}
