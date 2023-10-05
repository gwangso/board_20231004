package com.icia.board;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.service.BoardService;
import com.icia.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    private BoardDTO newBoard(int i){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("writer"+i);
        boardDTO.setBoardTitle("title"+i);
        boardDTO.setBoardContents("contents"+i);
        boardDTO.setBoardPass("1234");
        return boardDTO;
    }

    @Test
    @DisplayName("보드 데이터 붓기")
    public void dataInsert(){
        IntStream.rangeClosed(61,100).forEach(i ->{
            BoardDTO boardDTO = newBoard(i);
            boardService.save(boardDTO);
        });
    }


}
