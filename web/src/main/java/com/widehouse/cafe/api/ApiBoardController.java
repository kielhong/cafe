package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.service.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.persistence.GeneratedValue;

/**
 * Created by kiel on 2017. 2. 25..
 */
@RestController
@RequestMapping("api")
public class ApiBoardController {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeService cafeService;


    @GetMapping("/cafes/{cafeUrl}/boards/{boardId}")
    public Board getBoard(@PathVariable String cafeUrl,
                          @PathVariable Long boardId) {
        return boardRepository.findOne(boardId);
    }

    @GetMapping("/cafes/{cafeUrl}/boards")
    public List<Board> getBoardsByCafe(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);
        List<Board> boards = cafeService.listBoard(cafe);

        return boards;
    }
}
