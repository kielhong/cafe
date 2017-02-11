package com.widehouse.cafe.domain.member;

import com.widehouse.cafe.domain.cafe.Cafe;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
public class Member {
    private Long id;

    private String nickname;

    private List<Cafe> cafes;

    public Member() {
        this.cafes = new ArrayList<>();
    }

    public Member(String nickname) {
        this();
        this.nickname = nickname;
    }
}
