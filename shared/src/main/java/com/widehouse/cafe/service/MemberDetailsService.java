package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

/**
 * Created by kiel on 2017. 3. 3..
 */
@Service
public class MemberDetailsService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("Member Not Exists");
        }

        return member;
    }

    @Transactional
    public Member getCurrentMember() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Member) {
            return (Member)principal;
        } else {
            return null;
        }
    }
}
