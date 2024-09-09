package com.carbonhater.co2zerobookmark.board.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import com.carbonhater.co2zerobookmark.user.repository.entity.User;
import jakarta.persistence.*;

@Entity
public class Board extends BaseEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long boardId;
      private String boardTitle;
      private String boardContent;

}
