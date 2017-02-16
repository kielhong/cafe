package com.widehouse.cafe.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
