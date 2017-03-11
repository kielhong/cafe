package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleTagRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@Service
public class TagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private TagRepository tagRepository;

    public Tag getTagByName(String tagName) {
        Tag tag = tagRepository.findByName(tagName.trim());

        return tag;
    }

    public List<Tag> getTagsByCafe(Cafe cafe) {
        return articleTagRepository.findTagsByCafe(cafe);
    }

    public List<Article> getArticlesByTag(Cafe cafe, Tag tag) {
        return articleTagRepository.findArticlesByCafeAndTag(cafe, tag);
    }
}
