package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkCreateDTO;
import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkUpdateDTO;
import com.carbonhater.co2zerobookmark.bookmark.repository.BookmarkHistoryRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.BookmarkRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.*;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final FolderRepository folderRepository;
    private final BookmarkHistoryRepository bookmarkHistoryRepository;

    public List<Bookmark> findAllByFolder(Folder folder) {
        return bookmarkRepository.findActiveByFolder(folder);
    }

    // 삭제되지 않은 북마크 조회
    public List<Bookmark> getActiveBookmarks() {
        return bookmarkRepository.findByDeletedYn('N');
    }

    // 북마크 생성
    @Transactional
    public Bookmark createBookmark(BookmarkCreateDTO dto,Long userId){
        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkName(dto.getBookmarkName());
        bookmark.setBookmarkUrl(dto.getBookmarkUrl());
        bookmark.setCreatedId(1L); // 임의설정 ID:1
        bookmark.setModifiedId(1L);
        // folder 처리 로직 필요

        // 폴더 처리 로직
        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new NotFoundException("폴더를 찾을 수 없습니다."));
        bookmark.setFolder(folder);

        // 현재 시간 기록
        LocalDateTime now = LocalDateTime.now();
        bookmark.setCreatedAt(now);
        bookmark.setModifiedAt(now);

        // 북마크 히스토리 생성(저장)
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        saveBookmarkHistory(savedBookmark, savedBookmark.getCreatedAt(), savedBookmark.getModifiedAt());

        return savedBookmark;
    }

    // 북마크 수정
    @Transactional
    public Bookmark updateBookmark(Long bookmarkId, BookmarkUpdateDTO dto) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("북마크를 찾을 수 없습니다."));

        // 삭제된 북마크는 수정할 수 없도록 처리하는 로직
        if(bookmark.getDeletedYn() == 'Y') {
            throw new IllegalArgumentException("삭제된 북마크는 수정할 수 없습니다.");
        }

        bookmark.setBookmarkName(dto.getBookmarkName());
        bookmark.setBookmarkUrl(dto.getBookmarkUrl());

        // 폴더 처리 로직
        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new EntityNotFoundException("폴더를 찾을 수 없습니다."));
        bookmark.setFolder(folder);

        LocalDateTime now = LocalDateTime.now();
        bookmark.setCreatedAt(now);
        bookmark.setModifiedAt(now);

        // 북마크 및 히스토리 저장
        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);

        saveBookmarkHistory(updatedBookmark, updatedBookmark.getCreatedAt(), updatedBookmark.getModifiedAt());

        return updatedBookmark;
    }

    // 북마크 논리 삭제
    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("북마크를 찾을 수 없습니다.")); // 예외 처리 코드

        //현재 시간 기록
        LocalDateTime now = LocalDateTime.now();

        bookmark.setDeletedYn('Y');

        bookmark.setModifiedAt(now);

        bookmarkRepository.save(bookmark);
        saveBookmarkHistory(bookmark, now, now);
    }


    // 북마크 히스토리 저장
    private void saveBookmarkHistory(Bookmark bookmark, LocalDateTime createdAt, LocalDateTime modifiedAt){
        BookmarkHistory bookmarkHistory = new BookmarkHistory();
        bookmarkHistory.setBookmark(bookmark);
        bookmarkHistory.setFolderId(bookmark.getFolder().getFolderId());
        bookmarkHistory.setBookmarkName(bookmark.getBookmarkName());
        bookmarkHistory.setBookmarkUrl(bookmark.getBookmarkUrl());
        bookmarkHistory.setLastVisitedDatetime(bookmark.getLastVisitedAt());
        bookmarkHistory.setDeletedYn(bookmark.getDeletedYn());
        bookmarkHistory.setCreatedAt(createdAt);
        bookmarkHistory.setCreatedId(bookmark.getCreatedId());
        bookmarkHistory.setModifiedAt(modifiedAt);
        bookmarkHistory.setModifiedId(bookmark.getModifiedId());

        bookmarkHistoryRepository.save(bookmarkHistory);
    }


    // 북마크 클릭 시 마지막 방문일시 업데이트
    @Transactional
    public Bookmark clickBookmark(Long bookmarkId){
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new EntityNotFoundException("북마크를 찾을 수 없습니다."));

        // 삭제된 북마크는 수정할 수 없도록 처리하는 로직
        if(bookmark.getDeletedYn() == 'Y') {
            throw new IllegalArgumentException("삭제된 북마크는 클릭할 수 없습니다.");
        }

        // 마지막 방문일시 최신화
        LocalDateTime now = LocalDateTime.now();
        bookmark.setLastVisitedAt(now);

        // 북마크 및 히스토리 저장
        Bookmark updatedBookmark = bookmarkRepository.save(bookmark);

        saveBookmarkHistory(updatedBookmark, updatedBookmark.getCreatedAt(), updatedBookmark.getModifiedAt());

        return updatedBookmark;
    }

    // 쿼리 호출
    public List<Bookmark> getBookmarks(String bookmarkName, int offset, int limit, String sort, String order) {
        return bookmarkRepository.searchBookmarks(bookmarkName, sort, order, offset, limit);
    }


}
