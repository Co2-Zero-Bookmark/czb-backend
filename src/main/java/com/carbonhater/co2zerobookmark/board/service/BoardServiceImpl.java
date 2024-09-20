package com.carbonhater.co2zerobookmark.board.service;


import com.carbonhater.co2zerobookmark.board.model.BoardRequestDTO;
import com.carbonhater.co2zerobookmark.board.model.BoardResponseDTO;
import com.carbonhater.co2zerobookmark.board.model.LikeRequestDTO;
import com.carbonhater.co2zerobookmark.board.repository.BoardRepository;
import com.carbonhater.co2zerobookmark.board.repository.LikeRepository;
import com.carbonhater.co2zerobookmark.board.repository.entity.Board;
import com.carbonhater.co2zerobookmark.board.repository.entity.Like;
import com.carbonhater.co2zerobookmark.bookmark.model.dto.FolderHierarchyDto;
import com.carbonhater.co2zerobookmark.bookmark.service.FolderService;
import com.carbonhater.co2zerobookmark.common.exception.CustomRuntimeException;
import com.carbonhater.co2zerobookmark.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final FolderService folderService;
    private final LikeRepository likeRepository;

    @Override
    public List<BoardResponseDTO> getAllBoards(Long userId) {
        List<Board> boards = boardRepository.findAllBoardsByDeletedYn('N');

        // 리스트가 비어있는지 확인하고 예외 던지기
        if (boards.isEmpty()) {
            throw new NotFoundException("게시글이 존재하지 않습니다");
        }
        // 유저가 좋아요를 누른 게시글을 찾아내기
        List<Like> likes = likeRepository.findAllByUserIdAndDeletedYn(userId, 'N');
        // 좋아요를 누른 게시글의 ID를 저장할 맵
        Map<Long, Boolean> userLikeStatus = new HashMap<>();
        // 좋아요를 누른 게시글의 ID를 맵에 저장
        likes.forEach(like -> userLikeStatus.put(like.getBoardId(), true));

        // 전체 보드 리스트를 변환하여 반환
        return boards.stream()
                .map(board -> {
                    long likeCount = likeRepository.countByBoardIdAndDeletedYn(board.getBoardId(), 'N');
                    boolean isLiked = userLikeStatus.getOrDefault(board.getBoardId(), false);
                    log.info("isLiked : {}", isLiked);
                    return BoardResponseDTO.builder()
                            .boardId(board.getBoardId())
                            .userId(board.getUserId())
                            .boardTitle(board.getBoardTitle())
                            .boardContent(board.getBoardContent())
                            .boardLikeCount(likeCount)
                            .boardIsLiked(isLiked)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public BoardResponseDTO createBoard(BoardRequestDTO boardRequestDTO) {
        Board board = boardRepository.save(Board.builder()
                .userId(boardRequestDTO.getUserId())
                .boardTitle(boardRequestDTO.getBoardTitle())
                .boardContent(boardRequestDTO.getBoardContent())
                .rootFolderId(boardRequestDTO.getParentFolderId())
                .build());

        return BoardResponseDTO.builder()
                .boardId(board.getBoardId())
                .userId(board.getUserId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .rootFolderId(board.getRootFolderId())
                .build();
    }

    @Override
    public BoardResponseDTO getBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeletedYn(boardId, 'N')
                .map(board -> {
                    long likeCount = likeRepository.countByBoardIdAndDeletedYn(board.getBoardId(), 'N');
                    boolean isLiked = likeRepository.findByBoardIdAndUserIdAndDeletedYn(board.getBoardId(), board.getUserId(), 'N').isPresent();
                    FolderHierarchyDto folderHierarchyDto = folderService.getFolderHierarchyByParentFolderId(board.getRootFolderId());
                    return BoardResponseDTO.builder()
                            .boardId(board.getBoardId())
                            .userId(board.getUserId())
                            .boardTitle(board.getBoardTitle())
                            .boardContent(board.getBoardContent())
                            .rootFolderId(board.getRootFolderId())
                            .boardIsLiked(isLiked)
                            .boardLikeCount(likeCount)
                            .folderHierarchyDto(folderHierarchyDto)
                            .build();
                })
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
    }

    @Override
    public String downloadBoard(Long parentFolderId, Long userId) {
        FolderHierarchyDto folderHierarchyDto = folderService.getFolderHierarchyByParentFolderId(parentFolderId);
        if (folderHierarchyDto == null) {
            throw new NotFoundException("폴더 계층 구조를 가져오는데 실패했습니다.");
        }
        folderService.copyParentFolder(parentFolderId, userId);
        return "폴더 계층 구조를 다운로드했습니다.";
    }

    @Override
    public String likeBoard(LikeRequestDTO likeRequestDTO) {
        // 이전에 userId와 BoardId가 유효한지 봐야 한다.
        boardRepository.findByBoardIdAndDeletedYn(likeRequestDTO.getBoardId(), 'N')
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));


        try {
            likeRepository.save(Like.builder()
                    .boardId(likeRequestDTO.getBoardId())
                    .userId(likeRequestDTO.getUserId())
                    .build());
            return "좋아요에 성공했습니다.";
        } catch (DataIntegrityViolationException e) {
            throw new CustomRuntimeException("좋아요 저장 중 데이터 무결성 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new CustomRuntimeException("좋아요 저장 중 오류가 발생했습니다.");
        }
    }

    @Override
    public String dislikeBoard(LikeRequestDTO likeRequestDTO) {
        boardRepository.findByBoardIdAndDeletedYn(likeRequestDTO.getBoardId(), 'N')
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));

        Optional<Like> likeOptional = likeRepository.findByBoardIdAndUserIdAndDeletedYn(
                likeRequestDTO.getBoardId(),
                likeRequestDTO.getUserId(),
                'N'
        );

        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();
            like.setDeletedYn('Y');
            likeRepository.save(like);
        } else {
            throw new NotFoundException("좋아요가 설정되어 있지 않았습니다.");
        }

        return "좋아요 해제에 성공했습니다.";
    }


}
