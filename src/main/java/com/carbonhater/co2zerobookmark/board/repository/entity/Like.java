package com.carbonhater.co2zerobookmark.board.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "`like`")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    private Long userId;
    private Long boardId;
}
