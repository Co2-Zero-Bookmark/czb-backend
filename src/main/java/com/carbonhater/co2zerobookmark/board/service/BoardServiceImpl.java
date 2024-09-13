package com.carbonhater.co2zerobookmark.board.service;


import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.LikeRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    @Override
    public List<BoardResponseDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAllBoardsByDeletedYn('N');

        // 리스트가 비어있는지 확인하고 예외 던지기
        if (boards.isEmpty()) {
            throw new NotFoundException("게시판이 존재하지 않습니다");
        }

        // 전체 보드 리스트를 변환
        List<BoardResponseDTO> boardResponseDTOs = boards.stream()
                .map(board -> {
                    long likeCount = likeRepository.countByBoardIdAndDeletedYn(board.getBoardId(), 'N');
                    return BoardResponseDTO.builder()
                            .boardId(board.getBoardId())
                            .userId(board.getUserId())
                            .boardTitle(board.getBoardTitle())
                            .boardContent(board.getBoardContent())
                            .boardLikeCount(likeCount)
                            .build();
                })
                .collect(Collectors.toList());

        return boardResponseDTOs;
    }
}
