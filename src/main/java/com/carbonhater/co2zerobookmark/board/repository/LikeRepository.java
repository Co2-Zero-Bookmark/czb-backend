package com.carbonhater.co2zerobookmark.board.repository;


import com.carbonhater.co2zerobookmark.board.repository.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByBoardIdAndDeletedYn(Long boardId, char deletedYn);

    List<Like> findAllByUserIdAndDeletedYn(Long userId, char n);

    Optional<Like> findByBoardIdAndUserIdAndDeletedYn(Long boardId, Long userId, char n);

    Object countByBoardId(long l);
}
