package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.article.ArticleTagRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.cafe.Cafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService tagService;
    @MockBean
    private ArticleTagRepository articleTagRepository;

    @Test
    public void getTagsByCafe_Should_ListTagsOfCafe() {
        // Given
        Cafe cafe = new Cafe("testurl", "testname");
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        given(articleTagRepository.findAllByCafe(cafe))
                .willReturn(Arrays.asList(tag1, tag2));
        // when
        List<Tag> tags = tagService.getTagsByCafe(cafe);
        // then
        then(tags)
                .contains(tag1, tag2);
    }
}
