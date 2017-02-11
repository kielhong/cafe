package com.widehouse.cafe.domain.cafe;

import com.widehouse.cafe.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 10..
 */
@NoArgsConstructor
@Getter
public class CafeMember {
    private Cafe cafe;

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
