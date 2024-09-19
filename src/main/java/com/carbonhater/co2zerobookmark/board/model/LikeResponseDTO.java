package com.carbonhater.co2zerobookmark.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class LikeResponseDTO {
    private Long likeId;
    private Long boardId;
    private Long userId;
}
