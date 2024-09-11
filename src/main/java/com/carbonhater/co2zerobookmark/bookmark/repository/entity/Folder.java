package com.carbonhater.co2zerobookmark.bookmark.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @OneToMany(mappedBy = "folder")
    private List<Folder> subFolders = new ArrayList<>(); // 하위 폴더 리스트 (양방향 매핑)

    @Builder
    public Folder(Long folderId, Folder folder, Long userId, Tag tag, String folderName) {
        this.folderId = folderId;
        this.folder = folder;
        this.userId = userId;
        this.tag = tag;
        this.folderName = folderName;
        //TODO Auditing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        setDeletedYN('N');
        setCreatedBy(now);
        setCreatedId(userId);
        setModifiedBy(now);
        setModifiedId(userId);
    }

    public void update(Folder parentFolder, Tag tag, String folderName, Long userId) {
        this.folder = parentFolder;
        this.tag = tag;
        this.folderName = folderName;
        //TODO Auditing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        setModifiedBy(now);
        setModifiedId(userId);
    }

    public void delete(Long userId) {
        setDeletedYN('Y');
        //TODO Auditing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        setModifiedBy(now);
        setModifiedId(userId);
    }
}
