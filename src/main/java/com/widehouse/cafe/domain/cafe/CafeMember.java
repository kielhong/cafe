package com.widehouse.cafe.domain.cafe;

import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@Getter
public class CafeMember {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cafe cafe;

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
