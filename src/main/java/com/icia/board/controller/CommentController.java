package com.icia.board.controller;

import com.icia.board.dto.CommentDTO;
import com.icia.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody CommentDTO commentDTO,
                               @RequestParam("boardId") Long boardId){
        commentService.save(commentDTO, boardId);
        List<CommentDTO> commentDTOList = commentService.findAll(boardId);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

}
