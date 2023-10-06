package com.icia.board;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.repository.BoardFileRepository;
import com.icia.board.service.BoardService;
import com.icia.board.repository.BoardRepository;
import com.icia.board.util.UtilClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardFileRepository boardFileRepository;

    private BoardDTO newBoard(int i){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("writer"+i);
        boardDTO.setBoardTitle("title"+i);
        boardDTO.setBoardContents("contents"+i);
        boardDTO.setBoardPass("1234");
        return boardDTO;
    }

    @Test
    @DisplayName("테스트 보드 데이터 저장")
    public void dataInsert(){
        IntStream.rangeClosed(61,100).forEach(i ->{
            BoardDTO boardDTO = newBoard(i);
            try {
                boardService.save(boardDTO);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    @DisplayName("보드 페이지 확인")
    public void pagingMethod(){
        int page=5; // db에서는 page가 0부터 시작한다.
        int pageLimit=5;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        // Page<boardEntity> ->Page<BoardDTO>
        // map boardEntities에 있는 모든 변수들을 사용할 수 있게 해주는 역할
        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
            BoardDTO.builder()
                    .id(boardEntity.getId())
                    .boardTitle(boardEntity.getBoardTitle())
                    .boardWriter(boardEntity.getBoardWriter())
                    .boardHits(boardEntity.getBoardHits())
                    .createdAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()))
                    .build());
        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }

    @Test
    @DisplayName("검색 메서드 확인")
    public void searchMethod() {
        // 제목에 1이 포함된 검색 결과
//        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrderByIdDesc("1");
//        // BoardEntity를 출력하지 않고 각각을 DTO로 변환해서 출력
//        boardEntityList.forEach(boardEntity -> {
//            System.out.println(BoardDTO.toBoardDTO(boardEntity));
//        });

        //제목에 1이 포함된 결과 페이징
        int page = 0;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities =
                boardRepository.findByBoardTitleContaining("1", PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // 제목, 또는 작성자에 1이
        String query = "1";
        boardEntities = boardRepository.findByBoardTitleContainingOrBoardWriterContaining(query, query, PageRequest.of(page,pageLimit,Sort.by(Sort.Direction.DESC,"id")));

        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()))
                        .build());
        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }

    @Test
    @Transactional // 부모테이블에서 자식테이블을 바로 참조할 때
    @DisplayName("참조관계 확인")
    public void findTest(){
        // BoardEntity 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(69L);
        BoardEntity boardEntity = optionalBoardEntity.get();
        // BoardEntity 에서 BoardFileEntity 조회
        List<BoardFileEntity> boardFileEntityList = boardEntity.getBoardFileEntityList();
        boardFileEntityList.forEach(boardFileEntity -> {
            System.out.println(boardFileEntity.getOriginalFileName());
            System.out.println(boardFileEntity.getStoredFileName());
        });
    }
}
