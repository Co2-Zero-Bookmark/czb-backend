package com.carbonhater.co2zerobookmark.board.service;


import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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

        return boardRepository.findAllBoardsByDeletedYn('N').stream()
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
    }
}
