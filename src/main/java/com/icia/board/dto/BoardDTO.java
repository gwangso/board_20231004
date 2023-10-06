package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.entity.CommentEntity;
import com.icia.board.util.UtilClass;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    private int fileAttached;
    private List<MultipartFile> boardFile;
    private List<String> originalFileName = new ArrayList<>();
    private List<String> storedFileName = new ArrayList<>();

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()));

        if (boardEntity.getFileAttached() == 1){
            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){
                boardDTO.getOriginalFileName().add(boardFileEntity.getOriginalFileName());
                boardDTO.getStoredFileName().add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setFileAttached(1);
        }else {
            boardDTO.setFileAttached(0);
        }

        return boardDTO;
    }
}
