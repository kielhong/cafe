package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.exception.BoardNotExistsException;
import com.widehouse.cafe.service.CafeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 25..
 */
@RestController
@RequestMapping("api")
public class ApiBoardController {
    @Autowired
    private CafeService cafeService;

    @GetMapping("cafes/{cafeUrl}/boards/{boardId}")
    public Board getBoard(@PathVariable String cafeUrl,
                          @PathVariable Long boardId) {
        Cafe cafe = cafeService.getCafe(cafeUrl);
        Board board = cafeService.getBoard(boardId);

        if (board.getCafe().equals(cafe)) {
            return board;
        } else {
            throw new BoardNotExistsException();
        }
    }

    @GetMapping("cafes/{cafeUrl}/boards")
    public List<Board> getBoardsByCafe(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return cafeService.listBoard(cafe);
    }
}
