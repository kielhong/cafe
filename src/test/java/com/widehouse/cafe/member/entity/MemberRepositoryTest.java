package com.widehouse.cafe.member.entity;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.member.entity.MemberRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Created by kiel on 2017. 3. 3..
 */
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findByUsername_thenMember() {
        Member member = new Member("foo", "passowrd", "nickname", "foo@bar.com");
        entityManager.persist(member);

        Member result = memberRepository.findByUsername("foo");

        then(result)
                .isEqualTo(member);
    }
}