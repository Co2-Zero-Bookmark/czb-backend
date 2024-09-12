package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Entity
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
        setDeletedYN('N');
        setCreatedBy(String.valueOf(now));
        setCreatedId(userId);
        setModifiedBy(String.valueOf(now));
        setModifiedId(userId);
    }

    public void update(Folder parentFolder, Tag tag, String folderName, Long userId, LocalDateTime now) {
        this.folder = parentFolder;
        this.tag = tag;
        this.folderName = folderName;
        //TODO Auditing
        setModifiedBy(String.valueOf(now));
        setModifiedId(userId);
    }

    public void delete(Long userId, LocalDateTime now) {
        setDeletedYN('Y');
        //TODO Auditing
        setModifiedBy(String.valueOf(now));
        setModifiedId(userId);
    }
}
