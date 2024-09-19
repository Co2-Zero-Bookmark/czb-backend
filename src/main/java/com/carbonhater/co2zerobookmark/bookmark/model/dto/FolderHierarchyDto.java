package com.carbonhater.co2zerobookmark.bookmark.model.dto;

import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Bookmark;
import com.carbonhater.co2zerobookmark.bookmark.repository.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class FolderHierarchyDto {

    private FolderDto parentFolder;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class FolderDto {
        private Long folderId;
        private String folderName;
        private TagDto tag;
        private List<FolderDto> subFolders;
        private List<BookmarkDto> bookmarks;

        public FolderDto(Folder folderEntity, List<Folder> subFolders, List<Bookmark> bookmarks) {
            this.folderId = folderEntity.getFolderId();
            this.folderName = folderEntity.getFolderName();
            this.tag = new TagDto(folderEntity.getTag());
            this.subFolders = subFolders.stream().map(subFolder -> new FolderDto(subFolder, List.of(), List.of())).collect(Collectors.toList());
            this.bookmarks = bookmarks.stream().map(BookmarkDto::new).collect(Collectors.toList());
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BookmarkDto {
        private Long bookmarkId;
        private String bookmarkName;
        private String bookmarkUrl;

        public BookmarkDto(Bookmark bookmark) {
            this.bookmarkId = bookmark.getBookmarkId();
            this.bookmarkName = bookmark.getBookmarkName();
            this.bookmarkUrl = bookmark.getBookmarkUrl();
        }
    }
}
