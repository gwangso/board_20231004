package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String save(@ModelAttribute BoardDTO boardDTO){
        boardService.save(boardDTO);
        return "redirect:/board";
    }

    @GetMapping
    public String findAll(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
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

    @GetMapping("/delete/{id}")
    public ResponseEntity axiosDelete(@PathVariable("id") Long id,
                                      @RequestParam("password") String password){
        boolean result = boardService.delete(id, password);
        if (result){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
