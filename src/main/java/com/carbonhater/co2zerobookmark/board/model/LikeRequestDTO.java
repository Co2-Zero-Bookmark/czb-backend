package com.carbonhater.co2zerobookmark.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LikeRequestDTO {
    private Long boardId;
    private Long userId;
}
