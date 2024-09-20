package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.TagDto;
import com.carbonhater.co2zerobookmark.bookmark.repository.TagRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Tag;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(Long id) {
        return tagRepository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException("Tag 에서 ID " + id + "를 찾을 수 없습니다."));
    }

    public Tag getTag(Long tagId) {
        return Optional.ofNullable(tagId)
                .map(this::getTagById)
                .orElse(null); // tagId가 null인 경우, null 반환 (태그 없음)
    }

    public List<TagDto> getTags() {
        return tagRepository.findActiveAll()
                .stream().map(TagDto::new).toList();
    }
}
