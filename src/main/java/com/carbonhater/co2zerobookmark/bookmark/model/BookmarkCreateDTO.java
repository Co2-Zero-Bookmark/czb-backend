package com.carbonhater.co2zerobookmark.bookmark.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkCreateDTO {

    private String bookmarkName;
    private String bookmarkUrl;
    private Long folderId;
}
