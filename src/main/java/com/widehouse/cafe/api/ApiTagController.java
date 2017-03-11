package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RestController
@RequestMapping("api")
@Slf4j
public class ApiTagController {
    @Autowired
    public CafeService cafeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/cafes/{cafeUrl}/tags")
    public List<Tag> getCafeTags(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);
        List<Tag> tags = tagService.getTagsByCafe(cafe);

        return tags;
    }

    @GetMapping("/cafes/{cafeUrl}/tags/{tagName}/articles")
    public List<Article> getArticlesByTag(@PathVariable String cafeUrl,
                                          @PathVariable String tagName) {
        Cafe cafe = cafeService.getCafe(cafeUrl);
        Tag tag = tagService.getTagByName(tagName);

        log.debug("cafe : {}, {} ", cafe.getUrl(), cafe);
        log.debug("tag : {}", tag.getName());
        List<Article> articles = tagService.getArticlesByTag(cafe, tag);

        return articles;
    }


}
