package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Cafe;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kiel on 2017. 3. 10..
 */
@Service
@Slf4j
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public Tag getTagByName(String tagName) {
        return tagRepository.findByName(tagName.trim());
    }

    public List<Tag> getTagsByCafe(Cafe cafe) {
        return tagRepository.findAllByCafe(cafe);
    }

    public List<Article> getArticlesByTag(Cafe cafe, Tag tag) {
        return tagRepository.findArticlesByCafeAndTag(cafe, tag);
    }

    /**
     * update tags.
     * @param article Article
     * @param tagForms request form of tag
     */
    @Transactional
    public void updateTagsOfArticle(Article article, List<Tag> tagForms) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tagForm : tagForms) {
            Tag tag = tagRepository.findByName(tagForm.getName());
            if (tag == null) {
                tag = tagRepository.save(tagForm);
            }

            tags.add(tag);
        }
        article.getTags().clear();
        article.getTags().addAll(tags);
        articleRepository.save(article);
    }
}
