package com.carbonhater.co2zerobookmark.bookmark.model.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FoldersCreateDto {

    private final List<FolderUpdateDto> folders = new ArrayList<>();
}
