package com.widehouse.cafe.domain.cafe;

import com.widehouse.cafe.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface CafeMemberRepository extends JpaRepository<CafeMember, CafeMemberId> {

    @Query("SELECT cm.cafe FROM CafeMember cm WHERE cm.member = ?1")
    List<Cafe> findCafeByMember(Member member);
}
