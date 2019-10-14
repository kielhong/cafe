package com.widehouse.cafe.user.service;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.user.entity.User;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final CafeMemberRepository cafeMemberRepository;

    public List<Cafe> getCafesByUser(User user, Pageable pageable) {
        return cafeMemberRepository.findCafeByMember(user, pageable);
    }
}
