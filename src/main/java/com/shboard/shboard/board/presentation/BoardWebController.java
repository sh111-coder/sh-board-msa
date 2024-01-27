package com.shboard.shboard.board.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardWebController {

    @GetMapping("/boards")
    public String boards() {
        return "boards";
    }

    @GetMapping("/board-form")
    public String writeBoardForm() {
        return "write-board-form";
    }

    @GetMapping("/boards/{boardId}")
    public String boardDetail() {
        return "board-detail";
    }
}
