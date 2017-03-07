package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Cafe> getCafesByMember(Member member, Pageable pageable) {
        return cafeMemberRepository.findCafeByMember(member, pageable);
    }
}