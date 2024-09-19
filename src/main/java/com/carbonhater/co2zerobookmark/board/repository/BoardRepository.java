package com.carbonhater.co2zerobookmark.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllBoardsByDeletedYn(char deletedYn);

    Optional<Board> findByBoardIdAndDeletedYn(Long boardId, char deletedYn);
}
