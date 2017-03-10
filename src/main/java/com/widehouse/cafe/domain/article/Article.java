package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Article {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cafe cafe;

    @ManyToOne
    private Board board;

    @ManyToOne
    private Member writer;

    @Size(max = 500)
    private String title;

    @Size(max = 5000)
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ArticleTag> articleTags;

    private int commentCount;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public Article(Board board, Member writer, String title, String content) {
        this.cafe = board.getCafe();
        this.board = board;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.commentCount = 0;
        this.articleTags = new ArrayList<>();
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateDateTime = LocalDateTime.now();
    }

    public void moveBoard(Board board) {
        this.board = board;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public List<Tag> getTags() {
        return articleTags.stream()
                .map(x -> x.getTag())
                .collect(Collectors.toList());
    }
}
