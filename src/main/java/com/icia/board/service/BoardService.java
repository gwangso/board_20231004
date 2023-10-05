package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.PageDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.util.UtilClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public PageDTO pagination(int page) {
        int pageLimit = 5;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);

        int maxPage = (int) Math.ceil(boardRepository.count()/pageLimit);
        int endPage = ((page/pageLimit)+1)*pageLimit;
        int startPage = ((page/pageLimit)*pageLimit+1);

        pageDTO.setEndPage(endPage);
        pageDTO.setStartPage(startPage);
        pageDTO.setMaxPage(maxPage);

        return pageDTO;
    }

    public Page<BoardDTO> findAll(int page) {
        page = page - 1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()))
                        .build());
        return boardList;
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
