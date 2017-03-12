package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberDetailsService;
import com.widehouse.cafe.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private ArticleService articleService;
    @Autowired
    private MemberDetailsService memberDetailsService;

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

        List<Article> articles = tagService.getArticlesByTag(cafe, tag);

        return articles;
    }

    @GetMapping("/articles/{articleId}/tags")
    public List<Tag> getTags(@PathVariable Long articleId) {
        Member member = memberDetailsService.getCurrentMember();

        Article article = articleService.getArticle(articleId, member);

        return article.getTags();
    }

    @PostMapping("/articles/{articleId}/tags")
    public List<Tag> postTags(@PathVariable Long articleId,
                              @RequestBody List<Tag> tagForms) {
        Member member = memberDetailsService.getCurrentMember();

        Article article = articleService.getArticle(articleId, member);
        tagService.updateTagsOfArticle(article, tagForms);

        return article.getTags();
    }

}
