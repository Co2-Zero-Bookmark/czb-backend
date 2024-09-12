package com.carbonhater.co2zerobookmark.bookmark.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkUpdateDTO {

//    private Long bookmarkId;
    private String bookmarkUrl;
    private String bookmarkName;
    private Long folderId;

}
