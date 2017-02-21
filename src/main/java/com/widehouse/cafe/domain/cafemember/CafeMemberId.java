package com.widehouse.cafe.domain.cafemember;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created by kiel on 2017. 2. 15..
 */
@EqualsAndHashCode
@Getter
public class CafeMemberId implements Serializable {
    Cafe cafe;

    Member member;
}
