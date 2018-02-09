package com.widehouse.cafe.domain.cafemember;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@NoArgsConstructor
@Getter
public class CafeMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private Cafe cafe;

    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
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
