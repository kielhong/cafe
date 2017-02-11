package com.widehouse.cafe.domain.cafe;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 10..
 */
@AllArgsConstructor
@Getter
public class Cafe {
    private Long id;

    private String name;

    private List<CafeMember> cafeMembers = new ArrayList<>();

    public Cafe(String name) {
        this.name = name;
    }

    public void modifyName(String newName) {
        this.name = newName;
    }

    public CafeMember addMember(Member member) {
        CafeMember cafeMember = new CafeMember(this, member);
        if (cafeMembers.stream().noneMatch((x -> x.getMember().equals(member)))) {
            this.cafeMembers.add(cafeMember);
        } else {
            throw new CafeMemberAlreadyExistsException();
        }

        return cafeMember;
    }

    public void removeCafeMember(CafeMember cafeMember) {
        this.cafeMembers.remove(cafeMember);
    }
}
