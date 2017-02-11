package com.widehouse.cafe.domain.cafe;

import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
public class CafeMember {
    private Cafe cafe;

    private Member member;

    private LocalDateTime joinDate;

    public CafeMember(Cafe cafe, Member member) {
        this.cafe = cafe;
        this.member = member;
        this.joinDate = LocalDateTime.now();
    }
}
