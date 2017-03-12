package com.widehouse.cafe.domain.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by kiel on 2017. 3. 9..
 */
//@Entity

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag {
//    @Id @GeneratedValue
    private Long id;

//    @ManyToOne
    private Article article;

//    @ManyToOne
    private Tag tag;

    private LocalDateTime createDateTime;

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
        this.createDateTime = LocalDateTime.now();
    }

}
