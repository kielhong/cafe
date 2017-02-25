package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RestController
public class ArticleController {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ArticleService articleService;


    @GetMapping(value = "/cafes/{cafeUrl}/articles", params = {"page", "size"})
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        // TODO : artcleService 가 url을 받도록 하는게 좋을지?
        Cafe cafe = cafeRepository.findByUrl(cafeUrl);
        List<Article> articles = articleService.getArticlesByCafe(cafe, page, size);

        return articles;
    }

    @GetMapping(value = "/cafes/{cafeUrl}/boards/{boardId}/articles", params = {"page", "size"})
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @PathVariable Long boardId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        // TODO : artcleService 가 boardId을 받도록 하는게 좋을지?
        Board board = boardRepository.findOne(boardId);
        List<Article> articles = articleService.getArticlesByBoard(board, page, size);

        return articles;
    }
}
