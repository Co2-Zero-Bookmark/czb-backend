package com.carbonhater.co2zerobookmark.board.service;


import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.model.LikeRequestDTO;
import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.LikeRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;
import com.carbonhater.co2zerobookmark.board.repository.entity.Like;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
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
        List<Board> boards = boardRepository.findAllBoardsByDeletedYn('N');

        // 리스트가 비어있는지 확인하고 예외 던지기
        if (boards.isEmpty()) {
            throw new NotFoundException("게시판이 존재하지 않습니다");
        }

        // 전체 보드 리스트를 변환하여 반환
        return boards.stream()
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

    @Override
    public String likeBoard(LikeRequestDTO likeRequestDTO) {
        // 이전에 userId와 BoardId가 유효한지 봐야 한다.
        boardRepository.findByBoardIdAndDeletedYn(likeRequestDTO.getBoardId(), 'N')
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));

        Like like = likeRepository.save(Like.builder()
                .boardId(likeRequestDTO.getBoardId())
                .userId(likeRequestDTO.getUserId())
                .build());

        return "좋아요에 성공했습니다.";
    }

    @Override
    public String dislikeBoard(LikeRequestDTO likeRequestDTO) {
        boardRepository.findByBoardIdAndDeletedYn(likeRequestDTO.getBoardId(), 'N')
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));

        Like like = likeRepository.save(Like.builder()
                .userId(likeRequestDTO.getUserId())
                .boardId(likeRequestDTO.getBoardId())
                .deletedYn('Y')
                .build());

        return "좋아요 해제에 성공했습니다.";
    }
}
