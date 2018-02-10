package com.widehouse.cafe.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by kiel on 2017. 3. 15..
 */
@Document

@NoArgsConstructor
@Getter
public class SimpleMember {
    private Long id;

    private String username;

    public SimpleMember(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public SimpleMember(Member member) {
        this(member.getId(), member.getUsername());
    }
}
