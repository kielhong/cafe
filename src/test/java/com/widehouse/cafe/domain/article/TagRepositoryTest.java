package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void findTagByName_WhenTagName_Should_ReturnTag() {
        // given
        entityManager.persist(new Tag("tagname"));
        // when
        Tag result = tagRepository.findByName("tagname");
        // then
        then(result)
                .hasFieldOrPropertyWithValue("name", "tagname");
    }

}
