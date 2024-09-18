package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.ToString;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Entity
@ToString
public class Folder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder folder;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag")
    private Tag tag;

    private String folderName;

    @Builder
    public Folder(Long folderId, Folder folder, Long userId, Tag tag, String folderName, LocalDateTime now) {
        this.folderId = folderId;
        this.folder = folder;
        this.userId = userId;
        this.tag = tag;
        this.folderName = folderName;
        //TODO Auditing
        setDeletedYn('N');
        setCreatedAt(now);
        setCreatedId(userId);
        setModifiedAt(now);
        setModifiedId(userId);
    }

    public void update(Folder parentFolder, Tag tag, String folderName, Long userId, LocalDateTime now) {
        this.folder = parentFolder;
        this.tag = tag;
        this.folderName = folderName;
        //TODO Auditing
        setModifiedAt(now);
        setModifiedId(userId);
    }

    public void delete(Long userId, LocalDateTime now) {
        setDeletedYn('Y');
        //TODO Auditing
        setModifiedAt(now);
        setModifiedId(userId);
    }
}
