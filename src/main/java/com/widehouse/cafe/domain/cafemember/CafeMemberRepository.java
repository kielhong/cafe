package com.widehouse.cafe.domain.cafemember;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.domain.member.Member;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface CafeMemberRepository extends JpaRepository<CafeMember, CafeMemberId> {
    @Query("SELECT cm.cafe FROM CafeMember cm WHERE cm.member = ?1")
    List<Cafe> findCafeByMember(Member member, Pageable pageable);

    @Query("SELECT CASE WHEN count(cm) > 0 THEN true ELSE false END "
            + " FROM CafeMember cm WHERE cm.cafe = :cafe AND cm.member = :member")
    boolean existsByCafeMember(@Param("cafe") Cafe cafe, @Param("member") Member member);

    CafeMember findByCafeAndMember(Cafe cafe, Member member);
}
