package com.carbonhater.co2zerobookmark.board.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class BoardFolderRelation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private Long folderId;

}
