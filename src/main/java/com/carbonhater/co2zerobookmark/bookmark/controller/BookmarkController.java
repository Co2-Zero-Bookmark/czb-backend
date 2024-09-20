package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkCreateListDTO;
import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkUpdateDTO;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import com.carbonhater.co2zerobookmark.bookmark.service.BookmarkService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;


@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final SignService signService;


    // 북마크 생성
    @PostMapping("")
    public CustomResponseEntity<List<Bookmark>> createBookmarks(@RequestBody BookmarkCreateListDTO request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        List<Bookmark> bookmarks = request.getBookmarks().stream()
                .map(bookmark -> bookmarkService.createBookmark(bookmark, userId))
                .collect(Collectors.toList());
        return success(bookmarks);
    }

    // 북마크 수정
    @PutMapping("/{bookmarkId}")
    public CustomResponseEntity<Bookmark> updateBookmark(@PathVariable Long bookmarkId, @RequestBody BookmarkUpdateDTO dto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        Bookmark bookmark = bookmarkService.updateBookmark(bookmarkId, dto, userId);
        return success(bookmark);
    }

    // 북마크 삭제
    @DeleteMapping("/{bookmarkId}")
    public CustomResponseEntity<Void> deleteBookmark(@PathVariable Long bookmarkId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        bookmarkService.deleteBookmark(bookmarkId, userId);
        return success(null); // 삭제 후, 응답 본문이 없음을 나타냄
    }

    // 북마크 클릭
    @PostMapping("/{bookmarkId}/click")
    public CustomResponseEntity<Void> clickBookmark(@PathVariable Long bookmarkId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        bookmarkService.clickBookmark(bookmarkId, userId);
        return success(null);
    }

    // 북마크 조회
    @GetMapping
    public CustomResponseEntity<List<Bookmark>> getBookmarks(
            @RequestParam(value = "bookmarkName", required = false) String bookmarkName,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "lastVisitedAt") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        List<Bookmark> bookmarks = bookmarkService.getBookmarks(bookmarkName, userId, offset, limit, sort, order);
        return success(bookmarks);
    }




}
