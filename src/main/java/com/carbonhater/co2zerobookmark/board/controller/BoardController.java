package com.carbonhater.co2zerobookmark.board.controller;

import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.model.LikeRequestDTO;
import com.carbonhater.co2zerobookmark.board.service.BoardService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;

@Slf4j
@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public CustomResponseEntity<List<BoardResponseDTO>> getBoards(){
            Long userId = 2L;
            log.info("getBoards Controller");
            return success(boardService.getAllBoards(userId));
    }

    @PostMapping("/{boardId}/like")
    public CustomResponseEntity<String> likeBoard(@PathVariable Long boardId, @RequestBody LikeRequestDTO likeRequestDTO){
        likeRequestDTO.setBoardId(boardId);
        log.info("likeBoard Controller : {}", likeRequestDTO);
        return success(boardService.likeBoard(likeRequestDTO));
    }


    @DeleteMapping("/{boardId}/like")
    public CustomResponseEntity<String> dislikeBoard(@PathVariable Long boardId, LikeRequestDTO likeRequestDTO){
        likeRequestDTO.setBoardId(boardId);
        return success(boardService.dislikeBoard(likeRequestDTO));
    }
}
