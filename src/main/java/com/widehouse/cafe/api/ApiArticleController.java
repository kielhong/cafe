package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.MemberDetailsService;

import java.util.List;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RestController
@RequestMapping("api")
public class ApiArticleController {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MemberDetailsService memberDetailsService;

    @GetMapping("/cafes/{cafeUrl}/articles")
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Cafe cafe = cafeRepository.findByUrl(cafeUrl);

        return articleService.getArticlesByCafe(cafe, page, size);
    }

    @GetMapping("/cafes/{cafeUrl}/boards/{boardId}/articles")
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @PathVariable Long boardId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        // TODO : artcleService 가 boardId을 받도록 하는게 좋을지?
        Board board = boardRepository.findById(boardId).get();
        List<Article> articles = articleService.getArticlesByBoard(board, page, size);

        return articles;
    }

    @GetMapping("/cafes/{cafeUrl}/articles/{articleId}")
    public Article getArticle(@PathVariable String cafeUrl,
                              @PathVariable Long articleId) {
        Member reader = memberDetailsService.getCurrentMember();
        Article article = articleService.getArticle(articleId, reader);

        return article;
    }

    @PostMapping(value = "/cafes/{cafeUrl}/articles")
    public Article writeArticle(@PathVariable String cafeUrl,
                                @RequestBody ArticleForm articleForm) {
        Member member = memberDetailsService.getCurrentMember();
        Board board = boardRepository.findById(articleForm.getBoard().getId()).get();

        return articleService.writeArticle(board, member, articleForm.getTitle(), articleForm.getContent());
    }

    @Data
    public static class ArticleForm {
        String title;
        String content;
        BoardForm board;
    }

    @Data
    public static class BoardForm {
        Long id;
    }
}
