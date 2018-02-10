package com.widehouse.cafe.web;

import com.widehouse.cafe.annotation.CurrentMember;
import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.ArticleService;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.TagService;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public TagRepository tagRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;

    /**
     * GET /api/cafes/{cafeUrl}/tags.
     * List Tags of Cafe
     * @param cafeUrl url of cafe
     * @return list of {@link Tag}
     */
    @GetMapping("cafes/{cafeUrl}/tags")
    public List<Tag> getCafeTags(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return tagService.getTagsByCafe(cafe);
    }

    /**
     * GET /api/cafes/{cafeUrl}/tags/{tagName}/articles.
     * List Articles with Tag in Cafe
     * @param cafeUrl url of cafe
     * @param tagName name of tag
     * @return list of {@link Article}
     */
    @GetMapping("cafes/{cafeUrl}/tags/{tagName}/articles")
    public List<Article> getArticlesByTag(@PathVariable String cafeUrl,
                                          @PathVariable String tagName) {
        Cafe cafe = cafeService.getCafe(cafeUrl);
        Tag tag = tagService.getTagByName(tagName);

        return tagService.getArticlesByTag(cafe, tag);
    }

    /**
     * GET /api/articles/{articleId}/tags.
     * List Tags of Article
     * @param articleId id of article
     * @param member member who reads article
     * @return list of {@link Tag}
     */
    @GetMapping("articles/{articleId}/tags")
    public List<Tag> getTags(@PathVariable Long articleId,
                             @CurrentMember Member member) {
        Article article = articleService.getArticle(articleId, member);

        return article.getTags();
    }

    /**
     * POST /api/articles/{articleId}/tags.
     * Attach Tags to Article
     * @param articleId id of article
     * @param tagForms request form of tags
     * @param member member who is requesting
     * @return list of attached {@link Tag}
     */
    @PostMapping("/articles/{articleId}/tags")
    public List<Tag> postTags(@PathVariable Long articleId,
                              @RequestBody List<TagForm> tagForms,
                              @CurrentMember Member member) {
        Article article = articleService.getArticle(articleId, member);
        List<Tag> tags = new ArrayList<>();
        for (TagForm tagForm : tagForms) {
            Tag tag = tagService.getTagByName(tagForm.getName());
            if (tag == null) {
                tag = tagRepository.save(new Tag(tagForm.getName()));
            }
            tags.add(tag);
        }

        tagService.updateTagsOfArticle(article, tags);

        return article.getTags();
    }

    @Data
    public static class TagForm {
        String name;
    }
}