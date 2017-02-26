package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 25..
 */
@RestController
public class BoardController {
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/cafes/{cafeUrl}/boards/{boardId}")
    public Board getBoard(@PathVariable String cafeUrl,
                          @PathVariable Long boardId) {
        return boardRepository.findOne(boardId);
    }
}
