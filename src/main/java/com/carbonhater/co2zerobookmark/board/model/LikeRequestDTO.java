package com.carbonhater.co2zerobookmark.board.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LikeRequestDTO {
    private Long boardId;
    private Long userId;
}
