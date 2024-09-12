package com.carbonhater.co2zerobookmark.bookmark.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BookmarkCreateListDTO {
    private List<BookmarkCreateDTO> bookmarks;


}
