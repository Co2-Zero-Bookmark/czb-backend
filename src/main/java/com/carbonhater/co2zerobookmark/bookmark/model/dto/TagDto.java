package com.carbonhater.co2zerobookmark.bookmark.model.dto;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TagDto {
    private Long tagId;
    private String tagName;
    private String tagColorCode;
    private String tagDescription;

    public TagDto(Tag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
        this.tagColorCode = tag.getTagColorCode();
        this.tagDescription = tag.getTagDescription();
    }
}
