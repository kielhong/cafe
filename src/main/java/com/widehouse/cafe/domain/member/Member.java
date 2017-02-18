package com.widehouse.cafe.domain.member;

import com.widehouse.cafe.domain.cafe.Cafe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue
    private Long id;

    @Size(max = 30)
    private String nickname;

    public Member(String nickname) {
        this();
        this.nickname = nickname;
    }
}
