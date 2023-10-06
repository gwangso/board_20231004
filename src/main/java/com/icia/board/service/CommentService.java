package com.icia.board.service;

import com.icia.board.dto.CommentDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.CommentEntity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void save(CommentDTO commentDTO, Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
        commentRepository.save(commentEntity);
    }

    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());
        List<CommentEntity> commentEntities = commentRepository.findByBoardEntity(boardEntity);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentEntities.forEach(commentEntity -> {
            commentDTOList.add(CommentDTO.toDTO(commentEntity));
        });
        return commentDTOList;
    }
}
