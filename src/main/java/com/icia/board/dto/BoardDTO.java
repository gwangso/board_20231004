package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import com.icia.board.util.UtilClass;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder // 객체를 만들 때 객체를 만드는 코드 스타일, 기본생성자가 무력화됨
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardPass;
    private String boardContents;
    private String createdAt;
    private int boardHits;

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setId(boardEntity.getId());
//        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
//        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
//        boardDTO.setBoardPass(boardEntity.getBoardPass());
//        boardDTO.setBoardContents(boardEntity.getBoardContents());
//        boardDTO.setBoardHits(boardEntity.getBoardHits());
//        boardDTO.setCreatedAt(boardEntity.getCreatedAt());

//        String formattedDate = boardEntity.getCreatedAt().format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        boardDTO.setCreatedAt(formattedDate);
//        boardDTO.setCreatedAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()));

        BoardDTO boardDTO = BoardDTO.builder()
                .id(boardEntity.getId())
                .boardWriter(boardEntity.getBoardWriter())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContents(boardEntity.getBoardContents())
                .boardPass(boardEntity.getBoardPass())
                .boardHits(boardEntity.getBoardHits())
                .createdAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()))
                .build();
        return boardDTO;
    }
}
