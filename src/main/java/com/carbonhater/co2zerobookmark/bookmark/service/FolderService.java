package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.*;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderHistoryRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.FolderHistory;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Tag;
import com.carbonhater.co2zerobookmark.common.exception.BadRequestException;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderHistoryRepository folderHistoryRepository;
    private final TagService tagService;
    private final BookmarkService bookmarkService;

    public Folder getByFolderId(Long folderId) {
        return folderRepository.findActiveById(folderId)
                .orElseThrow(() -> new NotFoundException("Folder 에서 ID " + folderId + "를 찾을 수 없습니다."));
    }

    @Transactional
    public void createFolders(FoldersCreateDto foldersCreateDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        List<Folder> folders = new ArrayList<>();
        List<FolderHistory> histories = new ArrayList<>();
        for (FolderUpdateDto folder : foldersCreateDto.getFolders()) {
            if (Strings.isBlank(folder.getFolderName())) {
                throw new BadRequestException("폴더 이름은 필수입니다.");
            }
            Folder folderEntity = Folder.builder()
                    .folder(this.getParentFolder(folder.getParentFolderId(), userId))
                    .userId(userId)
                    .tag(tagService.getTag(folder.getTagId()))
                    .folderName(folder.getFolderName())
                    .now(now)
                    .build();
            folders.add(folderEntity);
            histories.add(FolderHistory.create(folderEntity, now));
        }

        folderRepository.saveAll(folders);
        folderHistoryRepository.saveAll(histories);
    }

    @Transactional
    public void updateFolder(long folderId, FolderUpdateDto folderUpdateDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Folder folder = getByFolderId(folderId);
        validateUserAccess(folder, userId);
        Folder parentFolder = getParentFolder(folderUpdateDto.getParentFolderId(), userId);
        Tag tag = tagService.getTag(folderUpdateDto.getTagId());

        folder.update(parentFolder, tag, folderUpdateDto.getFolderName(), userId, now);
        folderRepository.save(folder);
        folderHistoryRepository.save(FolderHistory.create(folder, now));
    }

    private Folder getParentFolder(Long parentFolderId, Long userId) {
        if (Objects.isNull(parentFolderId)) {
            return null; // ROOT 폴더인 경우, null 반환
        }

        Folder parentFolderEntity = getByFolderId(parentFolderId);
        validateUserAccess(parentFolderEntity, userId);

        return parentFolderEntity;
    }

    @Transactional
    public void deleteFolder(long folderId, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Folder folder = getByFolderId(folderId);
        validateUserAccess(folder, userId);

        // 재귀적으로 하위 폴더 및 관련 북마크 삭제
        deleteFolderAndSubFolders(folder, userId, now);
    }

    private void deleteFolderAndSubFolders(Folder folder, Long userId, LocalDateTime now) {
        // 하위 폴더 삭제 (재귀 호출)
        for (Folder subFolder : this.getActiveSubFolders(folder)) {
            deleteFolderAndSubFolders(subFolder, userId, now);
        }

        // 현재 폴더 삭제 처리
        folder.delete(userId, now);
        folderRepository.save(folder);
        folderHistoryRepository.save(FolderHistory.create(folder, now));

        // 폴더에 포함된 북마크 삭제
        bookmarkService.findAllByFolder(folder).stream()
                .map(Bookmark::getBookmarkId)
                .forEach(bookmarkId -> bookmarkService.deleteBookmark(bookmarkId, userId));
    }

    private List<Folder> getActiveSubFolders(Folder folder) {
        return folderRepository.findActiveByFolder(folder);
    }

    private void validateUserAccess(Folder folder, Long userId) {
        if (!Objects.equals(folder.getUserId(), userId)) {
            throw new BadRequestException("접근 불가능한 폴더입니다.");
        }
    }

    public FolderHierarchyDto getFolderHierarchyByParentFolderId(Long parentFolderId) {
        // 루트 폴더들을 가져옴 (parent folder가 null인 폴더들)
        Folder parentFolder = folderRepository.findActiveById(parentFolderId)
                .orElseThrow(() -> new NotFoundException("Folder 에서 ID " + parentFolderId + "를 찾을 수 없습니다."));

        // 폴더 계층 구조를 DTO로 변환
        return new FolderHierarchyDto(this.mapFolderToDto(parentFolder));
    }

    private FolderHierarchyDto.FolderDto mapFolderToDto(Folder folder) {
        // 하위 폴더 가져오기
        List<Folder> subFolders = this.getActiveSubFolders(folder);

        // 북마크 가져오기
        List<Bookmark> bookmarks = bookmarkService.findAllByFolder(folder);

        // Folder 변환
        FolderHierarchyDto.FolderDto folderDto = new FolderHierarchyDto.FolderDto();
        folderDto.setFolderId(folder.getFolderId());
        folderDto.setFolderName(folder.getFolderName());

        // Tag 변환
        if (folder.getTag() != null) {
            folderDto.setTag(new TagDto(folder.getTag()));
        }

        // 하위 폴더와 북마크 재귀적으로 DTO로 변환
        folderDto.setSubFolders(subFolders.stream()
                .map(this::mapFolderToDto)
                .collect(Collectors.toList()));

        folderDto.setBookmarks(bookmarks.stream()
                .map(FolderHierarchyDto.BookmarkDto::new)
                .collect(Collectors.toList()));

        return folderDto;
    }

    public List<FolderDto> getRootFoldersByUserId(Long userId) {
        return folderRepository.findByUserIdAndFolderIsNull(userId)
                .stream().map(FolderDto::new).toList();
    }
}
