package com.widehouse.cafe.domain.cafemember;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by kiel on 2017. 2. 15..
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CafeMemberId implements Serializable {
    private Long cafe;

    private Long member;
}
