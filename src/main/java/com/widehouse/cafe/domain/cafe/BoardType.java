package com.widehouse.cafe.domain.cafe;

import lombok.Getter;

/**
 * Created by kiel on 2017. 3. 12..
 */
@Getter
public enum BoardType {
    // Normal Boards
    LIST(false),
    MEMO(false),
    QNA(false),
    SALES(false),
    // Special Boards
    TAG(true),
    BEST(true),
    CALENDAR(true),
    BOOK(true);

    boolean specialType;

    BoardType(boolean specialType) {
        this.specialType = specialType;
    }
}
