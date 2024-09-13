package com.carbonhater.co2zerobookmark.board.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class BoardHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardHistoryId;
    private Long userId;
    private String boardTitle;
    private String boardContent;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
