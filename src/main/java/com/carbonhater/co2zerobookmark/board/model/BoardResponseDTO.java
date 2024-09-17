package com.carbonhater.co2zerobookmark.board.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardResponseDTO {

    private Long boardId;
    private Long userId;
    private String boardTitle;
    private String boardContent;
    private Long boardLikeCount;
    private Boolean boardIsLiked;

}
