package com.carbonhater.co2zerobookmark.bookmark.controller;

import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkCreateListDTO;
import com.carbonhater.co2zerobookmark.bookmark.model.BookmarkUpdateDTO;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import com.carbonhater.co2zerobookmark.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 생성
    @PostMapping("")
    public ResponseEntity<List<Bookmark>> createBookmarks(@RequestBody BookmarkCreateListDTO request){
        List<Bookmark> bookmarks = request.getBookmarks().stream()
                .map(bookmarkService::createBookmark)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookmarks);
    }

    // 북마크 수정
    @PutMapping("/{bookmarkId}")
    public ResponseEntity<Bookmark> updateBookmark(@PathVariable Long bookmarkId, @RequestBody BookmarkUpdateDTO dto){
        Bookmark bookmark = bookmarkService.updateBookmark(bookmarkId, dto);
        return ResponseEntity.ok(bookmark);
    }

    // 북마크 삭제
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long bookmarkId){
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.noContent().build(); // 삭제 후, 응답 본문이 없음을 나타냄
    }

    // 북마크 클릭
    @PostMapping("/{bookmarkId}/click")
    public ResponseEntity<Void> clickBookmark(@PathVariable Long bookmarkId){
        bookmarkService.clickBookmark(bookmarkId);
        return ResponseEntity.ok().build();
    }



}
