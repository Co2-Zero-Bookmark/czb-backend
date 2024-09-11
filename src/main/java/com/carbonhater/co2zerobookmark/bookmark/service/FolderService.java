package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderUpdateDto;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FoldersCreateDto;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final TagService tagService;

    public Folder getByFolderId(Long folderId) {
        return folderRepository.findActiveById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder 에서 ID " + folderId + "를 찾을 수 없습니다.")); //TODO Exception Handler
    }

    @Transactional
    public void createFolders(FoldersCreateDto foldersCreateDto, Long userId) {
        List<Folder> entites = new ArrayList<>();
        for (FolderUpdateDto folder : foldersCreateDto.getFolders()) {
            if (Strings.isBlank(folder.getFolderName())) {
                throw new RuntimeException("폴더 이름은 필수입니다."); //TODO Exception Handler
            }
            entites.add(Folder.builder()
                    .folder(this.getParentFolder(folder.getParentFolderId(), userId))
                    .userId(userId)
                    .tag(tagService.getTag(folder.getTagId()))
                    .folderName(folder.getFolderName())
                    .build());
        }

        folderRepository.saveAll(entites);
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
}
