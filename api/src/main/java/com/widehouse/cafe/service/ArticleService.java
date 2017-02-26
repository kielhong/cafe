package com.widehouse.cafe.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.projection.ArticleProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<ArticleProjection> getArticlesByCafe(Cafe cafe, int page, int size) {
        List<ArticleProjection> articles = articleRepository.findByCafe(cafe,
                new PageRequest(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public List<Article> getArticlesByBoard(Board board, int page, int size) {
        List<Article> articles = articleRepository.findByBoard(board,
                new PageRequest(page, size, new Sort(DESC, "id")));

        return articles;
    }
}
