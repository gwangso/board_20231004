package com.icia.board.controller;

import com.icia.board.entity.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
}
