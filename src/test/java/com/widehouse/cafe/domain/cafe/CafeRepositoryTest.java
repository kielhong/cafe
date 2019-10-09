package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

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
public class CafeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeRepository cafeRepository;

    private Category category1;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;

    @BeforeEach
    public void setUp() {
        category1 = new Category("category1", 1);
        entityManager.persist(category1);

        cafe1 = new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1);
        cafe2 = new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1);
        cafe3 = new Cafe("test3", "test3", "", CafeVisibility.PUBLIC, category1);
        entityManager.persist(cafe1);
        entityManager.persist(cafe2);
        entityManager.persist(cafe3);
    }

    @Test
    public void findByCategory_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategory(category1);

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    public void findByCategoryId_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId());

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    public void findByCategory_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getData().setCafeMemberCount(10L);
        cafe2.getData().setCafeMemberCount(5L);
        cafe3.getData().setCafeMemberCount(2L);

        List<Cafe> result = cafeRepository.findByCategory(category1,
                PageRequest.of(0, 3, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));

        assertThat(result)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

    @Test
    public void findByCategoryId_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getData().setCafeMemberCount(10L);
        cafe2.getData().setCafeMemberCount(5L);
        cafe3.getData().setCafeMemberCount(2L);

        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId(),
                PageRequest.of(0, 3, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));

        assertThat(cafes)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

    @Test
    public void findCafeByUrlTest() {
        Cafe cafe = cafeRepository.findByUrl("test1");

        then(cafe).isEqualTo(cafe1);
    }
}
