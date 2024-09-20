package com.carbonhater.co2zerobookmark.board.controller;

import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.model.LikeRequestDTO;
import com.carbonhater.co2zerobookmark.board.repository.entity.Like;
import com.carbonhater.co2zerobookmark.board.service.BoardService;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import com.carbonhater.co2zerobookmark.user.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity.success;

@Slf4j
@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final SignService signService;

    @GetMapping("")
    public CustomResponseEntity<List<BoardResponseDTO>> getBoards(){
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = signService.getUserIdByEmail(userEmail);
            log.info("getBoards Controller ==== user Email: {}, userId: {}", userEmail, userId);
            return success(boardService.getAllBoards(userId));
    }

    @GetMapping("/{boardId}")
    public CustomResponseEntity<BoardResponseDTO> getBoard(@PathVariable Long boardId){

        log.info("getBoard Controller ==== boardId: {}", boardService.getBoard(boardId));
        return success(boardService.getBoard(boardId));
    }

    @PostMapping("/board")
    public CustomResponseEntity<BoardResponseDTO> createBoard(@RequestBody BoardRequestDTO boardRequestDTO){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        boardRequestDTO.setUserId(userId);
        log.info("createBoard Controller ====  userId: {}, boardRequestDTO: {}", userEmail, boardRequestDTO);
        return success(boardService.createBoard(boardRequestDTO));
    }

    @PostMapping("/{parentFolderId}")
    public CustomResponseEntity<String> downloadBoard(@PathVariable Long parentFolderId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        log.info("createBoard Controller ==== user Email: {}, userId: {}", userEmail, userId);
        return success(boardService.createBoard(folderId, userId));
    }

    @PostMapping("/{boardId}/like")
    public CustomResponseEntity<String> likeBoard(@PathVariable Long boardId){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = signService.getUserIdByEmail(userEmail);
        LikeRequestDTO likeRequestDTO = new LikeRequestDTO(boardId, userId);
        log.info("likeBoard Controller : {}, boardID: {}", userId, boardId);

        return success(boardService.likeBoard(likeRequestDTO));
    }

    @DeleteMapping("/{boardId}/like")
    public CustomResponseEntity<String> dislikeBoard(@PathVariable Long boardId, LikeRequestDTO likeRequestDTO){
        likeRequestDTO.setBoardId(boardId);
        return success(boardService.dislikeBoard(likeRequestDTO));
    }
}
