package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.TagDto;
import com.carbonhater.co2zerobookmark.bookmark.service.TagService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public CustomResponseEntity<List<TagDto>> getTags() {
        return success(tagService.getTags());
    }
}
