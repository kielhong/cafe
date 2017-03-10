package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.article.ArticleTagRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@Service
public class TagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;

    public List<Tag> getTagsByCafe(Cafe cafe) {
        return articleTagRepository.findAllByCafe(cafe);
    }
}
