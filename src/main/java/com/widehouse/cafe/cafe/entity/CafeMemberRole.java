package com.widehouse.cafe.cafe.entity;

/**
 * Created by kiel on 2017. 2. 11..
 */
public enum CafeMemberRole {
    MANAGER(99),
    SUB_MANAGER(10),
    MEMBER(1);

    private int level;

    CafeMemberRole(int level) {
        this.level = level;
    }
}
