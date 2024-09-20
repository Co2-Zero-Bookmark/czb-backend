package com.carbonhater.co2zerobookmark.board.service;

import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.model.LikeRequestDTO;

import java.util.List;

public interface BoardService {

    List<BoardResponseDTO> getAllBoards(Long userId);

    String likeBoard(LikeRequestDTO likeRequestDTO);

    String dislikeBoard(LikeRequestDTO likeRequestDTO);

    BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO);

    BoardResponseDTO getBoard(Long boardId);
}
