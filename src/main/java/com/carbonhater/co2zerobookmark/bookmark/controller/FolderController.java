package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderUpdateDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FoldersCreateDto;
import com.carbonhater.co2zerobookmark.bookmark.service.FolderService;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final SignService signService;

    @GetMapping
    public ResponseEntity<Object> getParentFolders() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        return ResponseEntity.ok().body(folderService.getRootFoldersByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFoldersByParentId(@PathVariable long id) {
        return ResponseEntity.ok().body(folderService.getFolderHierarchyByParentFolderId(id));
    }

    @PostMapping
    public ResponseEntity<Object> createFolders(@RequestBody FoldersCreateDto foldersCreateDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.createFolders(foldersCreateDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFolder(@PathVariable long id, @RequestBody FolderUpdateDto folderUpdateDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.updateFolder(id, folderUpdateDto, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFolder(@PathVariable long id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.deleteFolder(id, userId);
        return ResponseEntity.ok().build();
    }
}
