package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.projection.ArticleProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private TagRepository tagRepository;


    public List<ArticleProjection> getArticlesByCafe(Cafe cafe, int page, int size) {
        List<ArticleProjection> articles = articleRepository.findByBoardCafe(cafe,
                PageRequest.of(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public List<Article> getArticlesByBoard(Board board, int page, int size) {
        List<Article> articles = articleRepository.findByBoard(board,
                PageRequest.of(page, size, new Sort(DESC, "id")));

        return articles;
    }

    public Article getArticle(Long id, Member reader) {
        Article article = articleRepository.findById(id).get();

        if (isArticleReadable(article.getCafe(), reader)) {
            return article;
        } else {
            throw new NoAuthorityException();
        }
    }

    public Article writeArticle(Board board, Member writer, String title, String content) {
        if (!cafeMemberRepository.existsByCafeMember(board.getCafe(), writer)) {
            throw new NoAuthorityException();
        }

        Article article =  articleRepository.save(new Article(board, writer, title, content));

        Cafe cafe = article.getBoard().getCafe();
        cafe.getStatistics().increaseArticleCount();
        cafeRepository.save(cafe);

        return article;
    }

    @Transactional
    public void addTag(Article article, Tag tag) {
        article.getTags().add(tag);
        articleRepository.save(article);
        tag.getArticles().add(article);
        tagRepository.save(tag);
    }

    private boolean isArticleReadable(Cafe cafe, Member reader) {
        if (cafe.getVisibility() == PUBLIC) {
            return true;
        } else {
            return cafeMemberRepository.existsByCafeMember(cafe, reader);
        }
    }
}
