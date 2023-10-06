package com.icia.board.dto;

import com.icia.board.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private String createdAt;

    public static CommentDTO toDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        return commentDTO;
    }
}
