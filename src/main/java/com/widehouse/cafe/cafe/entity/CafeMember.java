package com.widehouse.cafe.cafe.entity;

import com.widehouse.cafe.user.entity.User;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CafeMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private Cafe cafe;

    @ManyToOne
    private User member;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CafeMemberRole role = CafeMemberRole.MEMBER;

    @Builder.Default
    private LocalDateTime joinDate = LocalDateTime.now();

//    /**
//     * constructor.
//     */
////    public CafeMember(Cafe cafe, User member, CafeMemberRole role) {
////        this.cafe = cafe;
////        this.member = member;
////        this.role = role;
////        this.joinDate = LocalDateTime.now();
////    }
////
////    public CafeMember(Cafe cafe, User member) {
////        this(cafe, member, CafeMemberRole.MEMBER);
////    }
}
