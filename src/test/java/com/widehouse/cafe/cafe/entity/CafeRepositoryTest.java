package com.widehouse.cafe.cafe.entity;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Created by kiel on 2017. 2. 15..
 */
@DataJpaTest
class CafeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeRepository cafeRepository;

    private Category category1;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;

    @BeforeEach
    void setUp() {
        category1 = new Category("category1", 1);
        entityManager.persist(category1);

        cafe1 = new Cafe("test1", "test1", "", PUBLIC, category1);
        cafe2 = new Cafe("test2", "test2", "", PUBLIC, category1);
        cafe3 = new Cafe("test3", "test3", "", PUBLIC, category1);
        entityManager.persist(cafe1);
        entityManager.persist(cafe2);
        entityManager.persist(cafe3);
    }

    @Test
    void findByCategory_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategory(category1);

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    void findByCategoryId_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId());

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    void findByCategory_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getData().setMemberCount(10L);
        cafe2.getData().setMemberCount(5L);
        cafe3.getData().setMemberCount(2L);

        List<Cafe> result = cafeRepository.findByCategory(category1,
                PageRequest.of(0, 3, Sort.by(DESC, "data.memberCount")));

        assertThat(result)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

    @Test
    void findByCategoryId_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getData().setMemberCount(10L);
        cafe2.getData().setMemberCount(5L);
        cafe3.getData().setMemberCount(2L);

        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId(),
                PageRequest.of(0, 3, Sort.by(DESC, "data.memberCount")));

        assertThat(cafes)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

    @Test
    void findCafeByUrlTest() {
        Cafe cafe = cafeRepository.findByUrl("test1");

        then(cafe).isEqualTo(cafe1);
    }
}
