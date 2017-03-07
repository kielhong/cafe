package com.widehouse.cafe.domain.member;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 3. 3..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findByUsername_Sould_Return_Member() {
        // given
        entityManager.persist(new Member("testuser"));
        // when
        Member member = memberRepository.findByUsername("testuser");
        // then
        then(member)
                .isNotNull();

    }
}