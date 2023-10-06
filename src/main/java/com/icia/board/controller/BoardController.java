package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String save(){
        return "boardPages/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/board";
    }

    /*
        rest api : 주소값만으로 데이터를 찾을 수 있게 해주는 것
        /board/10 => 10번 글
        /board/20 => 20번 글
        /member/5 => 5번 회원

        페이지를 경로값으로 넣는것은 부적합
        3페이지에 있는 15번글 조회
        /board/3/15
        따라서 페이지는 query String 방식을 사용한다.
        /board/15?page=3
    */

    @GetMapping
    public String findAll(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                          @RequestParam(value = "type", required = false, defaultValue = "boardTitle") String type,
                          @RequestParam(value = "query", required = false, defaultValue = "") String query,
                          Model model){
        Page<BoardDTO> boardDTOList = boardService.findAll(page, type, query);
        model.addAttribute("boardList", boardDTOList);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOList.getTotalPages();
//        if ((startPage + blockLimit - 1) < boardDTOS.getTotalPages()) {
//            endPage = startPage + blockLimit - 1;
//        } else {
//            endPage = boardDTOS.getTotalPages();
//        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("query", query);
        return "boardPages/boardList";
    }

    @GetMapping("{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model){
        boardService.increaseHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        return "boardPages/boardDetail";
    }

/*    @GetMapping("/delete/{id}")
    public ResponseEntity axiosDelete(@PathVariable("id") Long id,
                                      @RequestParam("password") String password){
        boolean result = boardService.delete(id, password);
        if (result){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable("id") Long id,
                                        @RequestParam("password") String password){
        boolean result = boardService.delete(id, password);
        if (result){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/update")
    public ResponseEntity update(@RequestParam("id") Long id,
                         @RequestParam("password") String password,
                         Model model){
        BoardDTO boardDTO = boardService.findByIdPassword(id,password);
        if(boardDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardPages/boardUpdate";
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody BoardDTO boardDTO){
        try{
            boardService.update(boardDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
