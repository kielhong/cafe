package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;

/**
 * Created by kiel on 2017. 3. 9..
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDateTime createDateTime;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;

    public Tag() {
        this.articles = new HashSet<>();
        this.createDateTime = LocalDateTime.now();
    }

    public Tag(String name) {
        this();
        this.name = name;
    }
}
