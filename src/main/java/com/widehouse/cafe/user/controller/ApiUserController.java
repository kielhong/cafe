package com.widehouse.cafe.user.controller;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.common.annotation.CurrentMember;
import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@RequestMapping("api")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/members/my/cafes")
    public List<Cafe> getCafesByUser(@PageableDefault(sort = "cafe.createDateTime", direction = Sort.Direction.DESC)
                                                   Pageable pageable,
                                       @CurrentMember User member) {
        return userService.getCafesByUser(member, pageable);
    }
}
