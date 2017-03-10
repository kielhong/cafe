package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Cleanup;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by kiel on 2017. 3. 9..
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
public class Tag {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDateTime createDateTime;

    @JsonIgnore
    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<ArticleTag> articleTags;

    public Tag() {
        this.articleTags = new HashSet<>();
        this.createDateTime = LocalDateTime.now();
    }

    public Tag(String name) {
        this();
        this.name = name;
    }
}
