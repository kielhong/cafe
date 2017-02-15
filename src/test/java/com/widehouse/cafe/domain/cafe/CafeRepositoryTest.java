package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CafeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeRepository cafeRepository;

    private CafeCategory category1;
    private CafeCategory category2;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;
    private Cafe cafe4;
    private Cafe cafe5;
    private Cafe cafe6;

    @Before
    public void setUp() {
        category1 = new CafeCategory("test1");
        category2 = new CafeCategory("test2");
        entityManager.persist(category1);
        entityManager.persist(category2);

        cafe1 = new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1);
        cafe2 = new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1);
        cafe3 = new Cafe("test3", "test3", "", CafeVisibility.PUBLIC, category1);
        cafe4 = new Cafe("test4", "test4", "", CafeVisibility.PUBLIC, category2);
        cafe5 = new Cafe("test5", "test5", "", CafeVisibility.PUBLIC, category2);
        cafe6 = new Cafe("test6", "test6", "", CafeVisibility.PUBLIC, category2);
        entityManager.persist(cafe1);
        entityManager.persist(cafe2);
        entityManager.persist(cafe3);
        entityManager.persist(cafe4);
        entityManager.persist(cafe5);
        entityManager.persist(cafe6);
    }

    @Test
    public void cafeList_by_category() {
        // when
        List<Cafe> cafes = cafeRepository.findByCategory(category1);
        // then
        assertThat(cafes)
                .hasSize(3)
                .containsOnly(cafe1, cafe2, cafe3)
                .doesNotContain(cafe4, cafe5, cafe6);
    }

    @Test
    public void cafeList_by_categoryId() {
        // when
        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId());
        // then
        assertThat(cafes)
                .hasSize(3)
                .containsOnly(cafe1, cafe2, cafe3)
                .doesNotContain(cafe4, cafe5, cafe6);
    }

    @Test
    public void cafeList_by_category_with_paging() {
        // when
        Pageable pageable = new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "name"));
        List<Cafe> cafes = cafeRepository.findByCategory(category1, pageable);
        // then
        assertThat(cafes)
                .hasSize(2)
                .containsExactly(cafe3, cafe2)
                .doesNotContain(cafe1, cafe4, cafe5, cafe6);
    }

}
