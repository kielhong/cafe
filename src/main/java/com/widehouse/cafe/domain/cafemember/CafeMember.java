package com.widehouse.cafe.domain.cafemember;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity
@IdClass(CafeMemberId.class)
@Getter
public class CafeMember {
    @Id
    @ManyToOne
    private Cafe cafe;

    @Id
    @ManyToOne
    private Member member;

    private CafeMemberRole role;

    private LocalDateTime joinDate;

    public CafeMember(Cafe cafe, Member member, CafeMemberRole role) {
        this.cafe = cafe;
        this.member = member;
        this.role = role;
        this.joinDate = LocalDateTime.now();
    }

    public CafeMember(Cafe cafe, Member member) {
        this(cafe, member, CafeMemberRole.MEMBER);
    }


}
