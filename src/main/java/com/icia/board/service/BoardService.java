package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.repository.BoardFileRepository;
import com.icia.board.repository.BoardRepository;
import com.icia.board.util.UtilClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        BoardEntity boardEntity = null;
        if (boardDTO.getBoardFile().get(0).isEmpty()){
            // 첨부파일 없음
            boardEntity = BoardEntity.toSaveEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            return savedId;
        }else {
            // 첨부파일 있음
            boardEntity = BoardEntity.toSaveEntityWithFile(boardDTO);
            // 게시글 저장처리 및 Entity 가져오기(id를 포함한 변수)
            BoardEntity savedEntity = boardRepository.save(boardEntity);
            // 파일 이름 처리, 파일 로컬에 저장 등

            // DTO에 담긴 파일 꺼내기
            List<MultipartFile> boardFileList = boardDTO.getBoardFile();

            for(MultipartFile boardFile : boardFileList){
                // 업로드한 파일 이름
                String originalFileName = boardFile.getOriginalFilename();
                // 저장용 파일 이름
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                // 저장경로 + 파일이름 준비
                String savePath = "D:\\springboot_img\\" + storedFileName;
                // 폴더에 파일 저장
                boardFile.transferTo(new File(savePath));
                // 파일 정보 board_file_table 에 저장
                BoardFileEntity boardFileEntity = BoardFileEntity.toSaveBoardFile(originalFileName, storedFileName, savedEntity);
                boardFileRepository.save(boardFileEntity);
            }

            return savedEntity.getId();
        }
    }

    public Page<BoardDTO> findAll(int page, String type, String query) {
        page = page - 1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities = null;

        if(query.equals("")){
            boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        }else{
            if(type.equals("boardTitle")){
                boardEntities = boardRepository.findByBoardTitleContaining(query, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
            }else if (type.equals("boardWriter")){
                boardEntities = boardRepository.findByBoardWriterContaining(query, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
            }
        }


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

    /**
     * 서비스 클래스 메서드에서 @Transactional을 붙이는 경우
     * 1. jpql로 작성한 메서드 호출할 때
     * 2. 부모엔티티에서 자식엔티티를 바로 호출할 때
     */

    @Transactional // jpql로 작성한 메서드 호출할 때
    public void increaseHits(Long id) {
        boardRepository.increaseHits(id);
    }

    @Transactional // 부모엔티티에서 자식엔티티를 바로 호출할 때
    public BoardDTO findById(Long id) {
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
