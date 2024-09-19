package com.carbonhater.co2zerobookmark.bookmark.model.dto;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import lombok.Getter;

@Getter
public class FolderDto {

    private final Long folderId;
    private final String folderName;
    private final TagDto tag;

    public FolderDto(Folder folder) {
        this.folderId = folder.getFolderId();
        this.folderName = folder.getFolderName();
        this.tag = folder.getTag() != null ? new TagDto(folder.getTag()) : null;
    }
}
