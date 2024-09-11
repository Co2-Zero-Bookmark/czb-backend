package com.carbonhater.co2zerobookmark.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
