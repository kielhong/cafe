package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CafeMemberRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    @Test
    public void findCafeByMember_should_list_cafes() {
        Member member = new Member("member");
        entityManager.persist(member);
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
        entityManager.persist(new CafeMember(cafe1, member));
        entityManager.persist(new CafeMember(cafe2, member));
        entityManager.persist(new CafeMember(cafe3, member));
        entityManager.persist(new CafeMember(cafe4, member));
        entityManager.persist(new CafeMember(cafe5, member));
        // when
        List<Cafe> cafes = cafeMemberRepository.findCafeByMember(member);
        // then
        assertThat(cafes)
                .contains(cafe1, cafe2, cafe3, cafe4, cafe5);

    }

}
