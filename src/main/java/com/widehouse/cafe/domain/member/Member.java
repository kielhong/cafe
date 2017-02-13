package com.widehouse.cafe.domain.member;

import com.widehouse.cafe.domain.cafe.Cafe;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@Getter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String nickname;

//    @ManyToMany
//    @JoinTable(name = "cafe_member",
//            joinColumns = { @JoinColumn(name = "member_id", nullable = false, updatable = false) },
//            inverseJoinColumns = { @JoinColumn(name = "cafe_id", nullable = false, updatable = false) })
//    private List<Cafe> cafes;

    public Member() {
//        this.cafes = new ArrayList<>();
    }

    public Member(String nickname) {
        this();
        this.nickname = nickname;
    }
}
