package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.TagService;
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
}
