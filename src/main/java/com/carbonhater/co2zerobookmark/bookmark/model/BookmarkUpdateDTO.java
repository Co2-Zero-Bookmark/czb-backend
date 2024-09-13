package com.carbonhater.co2zerobookmark.bookmark.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
public class BookmarkUpdateDTO {

    private String bookmarkUrl;
    private String bookmarkName;
    private Long folderId;

}
