package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

//        for (BoardEntity boardEntity : boardEntityList){
//            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
//            boardDTOList.add(boardDTO);
//        }

        boardEntityList.forEach(board -> {
            boardDTOList.add(BoardDTO.toBoardDTO(board));
        });

        return boardDTOList;
    }

    public BoardDTO findById(Long id) {
//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
//        if(optionalBoardEntity.isPresent()){
//            BoardEntity boardEntity = optionalBoardEntity.get();
//            return BoardDTO.toBoardDTO(boardEntity);
//        }else {
//            return null
//        }
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return BoardDTO.toBoardDTO(boardEntity);
    }

    public boolean delete(Long id, String boardPass) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
        if(boardDTO.getBoardPass().equals(boardPass)){

            boardRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
