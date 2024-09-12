package com.carbonhater.co2zerobookmark.Board;

import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void testFindAll(){
        //Given
        Board board1 = new Board(1L, 1L, "Title 1", "Content 1");
        Board board2 = new Board(2L, 1L, "Title 2", "Content 2");
        boardRepository.save(board1);
        boardRepository.save(board2);

        //When
        List<Board> results = boardRepository.findAll();

        //Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getBoardTitle()).isEqualTo("Title 1");
        assertThat(results.get(1).getBoardTitle()).isEqualTo("Title 2");
    }
}
