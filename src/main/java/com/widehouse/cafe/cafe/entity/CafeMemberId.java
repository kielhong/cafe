package com.widehouse.cafe.cafe.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
