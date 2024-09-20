package com.carbonhater.co2zerobookmark.bookmark.service;

import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkCreateDTO;
import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkUpdateDTO;
import com.carbonhater.co2zerobookmark.bookmark.repository.BookmarkHistoryRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.BookmarkRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.FolderRepository;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.*;
import com.carbonhater.co2zerobookmark.common.exception.IllegalParameterException;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
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
        bookmark.setCreatedId(userId); // 들어온 유저 ID
        bookmark.setModifiedId(userId);
        bookmark.setUserId(userId);
        // folder 처리 로직 필요

        // 폴더 처리 로직
        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new NotFoundException("폴더를 찾을 수 없습니다."));
        // 부모 폴더에 북마크 생성 불가 처리
        if(folder.getFolder() == null){
            throw new IllegalParameterException("상위 계층 폴더에는 생성할 수 없습니다.");
        }

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
    public Bookmark updateBookmark(Long bookmarkId, BookmarkUpdateDTO dto, Long userId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException("북마크를 찾을 수 없습니다."));

        // 삭제된 북마크는 수정할 수 없도록 처리하는 로직
        if(bookmark.getDeletedYn() == 'Y') {
            throw new IllegalParameterException("삭제된 북마크는 수정할 수 없습니다.");
        }

        bookmark.setBookmarkName(dto.getBookmarkName());
        bookmark.setBookmarkUrl(dto.getBookmarkUrl());
        bookmark.setModifiedId(userId);
        bookmark.setUserId(userId);

        // 폴더 처리 로직
        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new NotFoundException("폴더를 찾을 수 없습니다."));
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
    public void deleteBookmark(Long bookmarkId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException("북마크를 찾을 수 없습니다.")); // 예외 처리 코드

        //현재 시간 기록
        LocalDateTime now = LocalDateTime.now();

        bookmark.setDeletedYn('Y');
        bookmark.setModifiedId(userId);
        bookmark.setUserId(userId);
        bookmark.setModifiedAt(now);

        bookmarkRepository.save(bookmark);
        saveBookmarkHistory(bookmark, now, now);
    }


    // 북마크 히스토리 저장
    private void saveBookmarkHistory(Bookmark bookmark, LocalDateTime createdAt, LocalDateTime modifiedAt){
        BookmarkHistory bookmarkHistory = new BookmarkHistory();
        bookmarkHistory.setBookmark(bookmark);
        bookmarkHistory.setFolderId(bookmark.getFolder());
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
    public Bookmark clickBookmark(Long bookmarkId, Long userId){
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new NotFoundException("북마크를 찾을 수 없습니다."));

        // 삭제된 북마크는 수정할 수 없도록 처리하는 로직
        if(bookmark.getDeletedYn() == 'Y') {
            throw new IllegalParameterException("삭제된 북마크는 클릭할 수 없습니다.");
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
    public List<Bookmark> getBookmarks(String bookmarkName, Long userId, int offset, int limit, String sort, String order) {
        return bookmarkRepository.searchBookmarks(bookmarkName, userId, sort, order, offset, limit);
    }


}
