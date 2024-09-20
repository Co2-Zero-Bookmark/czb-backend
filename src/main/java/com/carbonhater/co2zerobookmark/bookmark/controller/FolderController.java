package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderHierarchyDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderUpdateDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FoldersCreateDto;
import com.carbonhater.co2zerobookmark.bookmark.service.FolderService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final SignService signService;

    @GetMapping
    public CustomResponseEntity<List<FolderDto>> getParentFolders() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        return success(folderService.getRootFoldersByUserId(userId));
    }

    @GetMapping("/{id}")
    public CustomResponseEntity<FolderHierarchyDto> getFoldersByParentId(@PathVariable long id) {
        return success(folderService.getFolderHierarchyByParentFolderId(id));
    }

    @PostMapping
    public CustomResponseEntity<Object> createFolders(@RequestBody FoldersCreateDto foldersCreateDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.createFolders(foldersCreateDto, userId);
        return success(null);
    }

    @PutMapping("/{id}")
    public CustomResponseEntity<Object> updateFolder(@PathVariable long id, @RequestBody FolderUpdateDto folderUpdateDto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.updateFolder(id, folderUpdateDto, userId);
        return success(null);
    }

    @DeleteMapping("/{id}")
    public CustomResponseEntity<Object> deleteFolder(@PathVariable long id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        folderService.deleteFolder(id, userId);
        return success(null);
    }
}
