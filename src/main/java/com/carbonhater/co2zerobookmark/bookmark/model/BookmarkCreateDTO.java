package com.carbonhater.co2zerobookmark.bookmark.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
public class BookmarkCreateDTO {

    private String bookmarkName;
    private String bookmarkUrl;
    private Long folderId;

}
