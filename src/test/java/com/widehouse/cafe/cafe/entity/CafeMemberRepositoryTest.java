package com.widehouse.cafe.cafe.entity;

import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.user.entity.User;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

/**
 * Created by kiel on 2017. 2. 15..
 */
@DataJpaTest
class CafeMemberRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    private User user;
    private Cafe cafe;

    @BeforeEach
    void setUp() {
        user = new User("user", "password");
        entityManager.persist(user);

        cafe = new Cafe("testurl", "testcafe");
        entityManager.persist(cafe);
    }

    @Test
    void findCafeByMember_should_list_cafes() {
        Cafe cafe1 = new Cafe("url1", "name1");
        entityManager.persist(cafe1);
        Cafe cafe2 = new Cafe("url2", "name2");
        entityManager.persist(cafe2);
        Cafe cafe3 = new Cafe("url3", "name3");
        entityManager.persist(cafe3);
        Cafe cafe4 = new Cafe("url4", "name4");
        entityManager.persist(cafe4);
        Cafe cafe5 = new Cafe("url5", "name5");
        entityManager.persist(cafe5);
        entityManager.persist(CafeMember.builder().cafe(cafe1).member(user).build());
        entityManager.persist(CafeMember.builder().cafe(cafe2).member(user).build());
        entityManager.persist(CafeMember.builder().cafe(cafe3).member(user).build());
        entityManager.persist(CafeMember.builder().cafe(cafe4).member(user).build());
        entityManager.persist(CafeMember.builder().cafe(cafe5).member(user).build());
        // when
        List<Cafe> cafes = cafeMemberRepository.findCafeByMember(user, PageRequest.of(0, 5));
        // then
        assertThat(cafes)
                .contains(cafe1, cafe2, cafe3, cafe4, cafe5);
    }

    @Test
    void existsCafeAndMember_WithCafeMember_Should_True() {
        // given
        entityManager.persist(CafeMember.builder().cafe(cafe).member(user).build());
        // when
        boolean exist = cafeMemberRepository.existsByCafeMember(cafe, user);
        // then
        then(exist)
                .isTrue();
    }

    @Test
    void existsCafeAndMember_WithNotCafeMember_Should_False() {
        // when
        boolean exist = cafeMemberRepository.existsByCafeMember(cafe, user);
        // then
        then(exist)
                .isFalse();
    }

    @Test
    void findByCafeAndMember_Should_Return_CafeMember() {
        // given
        CafeMember cafeMember = CafeMember.builder().cafe(cafe).member(user).role(MANAGER).build();
        entityManager.persist(cafeMember);
        // when
        CafeMember result = cafeMemberRepository.findByCafeAndMember(cafe, user);
        // then
        then(result)
                .isEqualTo(cafeMember);
    }
}
