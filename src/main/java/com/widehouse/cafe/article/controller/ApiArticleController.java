package com.widehouse.cafe.article.controller;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.service.ArticleService;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.service.CafeService;
import com.widehouse.cafe.common.annotation.CurrentMember;
import com.widehouse.cafe.member.entity.Member;

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
    private ArticleService articleService;
    @Autowired
    private CafeService cafeService;

    @GetMapping("/cafes/{cafeUrl}/articles")
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return articleService.getArticles(cafe, page, size);
    }

    @GetMapping("/cafes/{cafeUrl}/boards/{boardId}/articles")
    public List<Article> getArticlesByCafe(@PathVariable String cafeUrl,
                                           @PathVariable Long boardId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Board board = cafeService.getBoard(boardId);

        return articleService.getArticles(board, page, size);
    }

    @GetMapping("/cafes/{cafeUrl}/articles/{articleId}")
    public Article getArticle(@PathVariable String cafeUrl,
                              @PathVariable Long articleId,
                              @CurrentMember Member reader) {
        return articleService.getArticle(articleId, reader);
    }

    /**
     * POST /api/cafes/{careUrl}/articles.
     * Create article
     * @param cafeUrl url of cafe
     * @param articleForm request article form
     * @param member current member
     * @return created {@link Article}
     */
    @PostMapping(value = "/cafes/{cafeUrl}/articles")
    public Article writeArticle(@PathVariable String cafeUrl,
                                @RequestBody ArticleForm articleForm,
                                @CurrentMember Member member) {
        Board board = cafeService.getBoard(articleForm.getBoard().getId());

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