package com.widehouse.cafe.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by kiel on 2017. 3. 15..
 */
@Document
@NoArgsConstructor
@Getter
public class SimpleUser {
    private Long id;

    private String username;

    public SimpleUser(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public SimpleUser(User user) {
        this(user.getId(), user.getUsername());
    }
}
