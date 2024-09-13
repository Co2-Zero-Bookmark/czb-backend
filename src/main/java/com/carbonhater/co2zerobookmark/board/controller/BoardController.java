package com.carbonhater.co2zerobookmark.board.controller;

import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.service.BoardService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public CustomResponseEntity<List<BoardResponseDTO>> getBoards(){
            return success(boardService.getAllBoards());
    }
}
