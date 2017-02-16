package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;


    public List<Cafe> getCafesByMember(Member member) {
        return cafeMemberRepository.findCafeByMember(member);
    }
}
