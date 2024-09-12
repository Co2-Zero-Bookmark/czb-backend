package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderUpdateDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FoldersCreateDto;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderHistoryRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.FolderHistory;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderHistoryRepository folderHistoryRepository;
    private final TagService tagService;

    public Folder getByFolderId(Long folderId) {
        return folderRepository.findActiveById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder 에서 ID " + folderId + "를 찾을 수 없습니다.")); //TODO Exception Handler
    }

    @Transactional
    public void createFolders(FoldersCreateDto foldersCreateDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        List<Folder> folders = new ArrayList<>();
        List<FolderHistory> histories = new ArrayList<>();
        for (FolderUpdateDto folder : foldersCreateDto.getFolders()) {
            if (Strings.isBlank(folder.getFolderName())) {
                throw new RuntimeException("폴더 이름은 필수입니다."); //TODO Exception Handler
            }
            Folder folderEntity = Folder.builder()
                    .folder(this.getParentFolder(folder.getParentFolderId(), userId))
                    .userId(userId)
                    .tag(tagService.getTag(folder.getTagId()))
                    .folderName(folder.getFolderName())
                    .now(now)
                    .build();
            folders.add(folderEntity);
            histories.add(FolderHistory.builder()
                    .folder(folderEntity)
                    .now(now)
                    .build());
        }

        folderRepository.saveAll(folders);
        folderHistoryRepository.saveAll(histories);
    }

    @Transactional
    public void updateFolder(long folderId, FolderUpdateDto folderUpdateDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Folder folder = getByFolderId(folderId);
        if (!Objects.equals(folder.getUserId(), userId)) {
            throw new RuntimeException("접근 불가능한 폴더입니다."); //TODO Exception Handler
        }
        Folder parentFolder = getParentFolder(folderUpdateDto.getParentFolderId(), userId);
        Tag tag = tagService.getTag(folderUpdateDto.getTagId());
        folder.update(parentFolder, tag, folderUpdateDto.getFolderName(), userId, now);
        folderRepository.save(folder);
        folderHistoryRepository.save(FolderHistory.builder()
                .folder(folder)
                .now(now)
                .build());
    }

    private Folder getParentFolder(Long parentFolderId, Long userId) {
        if (Objects.isNull(parentFolderId)) {
            return null; // ROOT 폴더인 경우, null 반환
        }

        Folder parentFolderEntity = getByFolderId(parentFolderId);
        if (!Objects.equals(parentFolderEntity.getUserId(), userId)) {
            throw new RuntimeException("접근 불가능한 상위 폴더입니다."); //TODO Exception Handler
        }
        return parentFolderEntity;
    }

    @Transactional
    public void deleteFolder(long folderId, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Folder folder = getByFolderId(folderId);
        if (!Objects.equals(folder.getUserId(), userId)) {
            throw new RuntimeException("접근 불가능한 폴더입니다."); //TODO Exception Handler
        }
        //하위 폴더 삭제
        deleteSubFolders(folder, userId, now);

        //상위 폴더 삭제
        //TODO 폴더에 포함된 북마크 삭제
        folder.delete(userId, now);
        folderRepository.save(folder);
        folderHistoryRepository.save(FolderHistory.builder()
                .folder(folder)
                .now(now)
                .build());
    }

    @Transactional
    public void deleteSubFolders(Folder folder, Long userId, LocalDateTime now) {
        List<Folder> subFolders = folderRepository.findActiveSubFolders(folder.getFolderId());
        for (Folder subFolder : subFolders) {
            //TODO 폴더에 포함된 북마크 삭제
            deleteSubFolders(subFolder, userId, now);
            subFolder.delete(userId, now);
            folderRepository.save(subFolder);
            folderHistoryRepository.save(FolderHistory.builder()
                    .folder(subFolder)
                    .now(now)
                    .build());
        }
    }
}
