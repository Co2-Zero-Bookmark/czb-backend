package com.carbonhater.co2zerobookmark.Board;

import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.LikeRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;
import com.carbonhater.co2zerobookmark.board.service.BoardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBoards() {
        // Mock 데이터 생성
        Board board1 = new Board(1L, 1L, "Title 1", "Content 1");
        Board board2 = new Board(2L, 1L, "Title 2", "Content 2");

        // Mock 동작 정의
        when(boardRepository.findAll()).thenReturn(Arrays.asList(board1, board2));
        when(likeRepository.countByBoardId(1L)).thenReturn(5L);
        when(likeRepository.countByBoardId(2L)).thenReturn(3L);

        // 서비스 메소드 호출
        List<BoardResponseDTO> result = boardService.getAllBoards(2L);

        // 결과 검증
        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getBoardTitle());
        assertEquals(5, result.get(0).getBoardLikeCount());
        assertEquals("Title 2", result.get(1).getBoardTitle());
        assertEquals(3, result.get(1).getBoardLikeCount());
    }
}
