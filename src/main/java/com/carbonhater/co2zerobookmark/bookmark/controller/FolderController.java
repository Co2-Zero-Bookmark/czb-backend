package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderUpdateDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FoldersCreateDto;
import com.carbonhater.co2zerobookmark.bookmark.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<Object> createFolders(@RequestBody FoldersCreateDto foldersCreateDto) {
        Long userId = 0L; //TODO 스프링 시큐리티 개발필요
        folderService.createFolders(foldersCreateDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFolder(@PathVariable long id, @RequestBody FolderUpdateDto folderUpdateDto) {
        Long userId = 0L; //TODO 스프링 시큐리티 개발 필요
        folderService.updateFolder(id, folderUpdateDto, userId);
        return ResponseEntity.ok().build();
    }
}
