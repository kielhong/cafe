package com.widehouse.cafe.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
