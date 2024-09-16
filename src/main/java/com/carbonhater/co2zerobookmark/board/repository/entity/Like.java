package com.carbonhater.co2zerobookmark.board.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`like`")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    private Long userId;
    private Long boardId;

    @Builder
    public Like(Long boardId, Long userId, char deletedYn) {
        this.boardId = boardId;
        this.userId = userId;
        this.setDeletedYn(deletedYn);
    }
}
