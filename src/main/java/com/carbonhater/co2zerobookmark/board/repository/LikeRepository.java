package com.carbonhater.co2zerobookmark.board.repository;


import com.carbonhater.co2zerobookmark.board.repository.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByBoardId(Long boardId);
}
