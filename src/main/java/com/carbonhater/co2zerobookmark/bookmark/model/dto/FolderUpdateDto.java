package com.carbonhater.co2zerobookmark.bookmark.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FolderUpdateDto {

    private final Long parentFolderId;
    private final Long tagId;
    private final String folderName;

    @Builder
    FolderUpdateDto(Long parentFolderId, Long tagId, String folderName) {
        this.parentFolderId = parentFolderId;
        this.tagId = tagId;
        this.folderName = folderName;
    }
}
