package com.carbonhater.co2zerobookmark.board.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequestDTO {
    private Long parentFolderId;
    private Long userId;
    private String boardTitle;
    private String boardContent;
}
