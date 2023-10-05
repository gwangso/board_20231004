package com.icia.board.entity.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

    public BoardDTO findByIdPassword(Long id, String password) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
        if(password.equals(boardDTO.getBoardPass())){
            return boardDTO;
        }else{
            return null;
        }
    }

    /**
     * 서비스 클래스 메서드에서 @Transactional을 붙이는 경우
     * 1. jpql로 작성한 메서드 호출할 때
     * 2. 부모엔티티에서 자식엔티티를 바로 호출할 때
     */

    @Transactional
    public void increaseHits(Long id) {
        boardRepository.increaseHits(id);
    }

    public boolean delete(Long id, String password) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
        if(boardDTO.getBoardPass().equals(password)){
            boardRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public void update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
    }


}
