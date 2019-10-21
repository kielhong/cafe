package com.widehouse.cafe.comment.entity;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by kiel on 2017. 2. 20..
 */
@DataMongoTest
class CommentRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    private User user;

    @BeforeEach
     void init() {
        user = new User(1L, "member", "password");
    }

    @Test
     void saveCommentTest() {
        // given
        Comment comment = new Comment(1L, 1L, user, "comment");
        // when
        commentRepository.save(comment);
        // then
        int collectionSize = mongoTemplate.findAll(Comment.class).size();
        then(collectionSize)
                .isEqualTo(1);
    }

    @Test
     void saveReplyCommentsTest() {
        // given
        Long articleId = 2L;
        Comment comment = new Comment(1L, articleId, user, "comment");
        comment.getReplies().addAll(
                Arrays.asList(
                    new Comment(1L, articleId, user, "subcomment1"),
                    new Comment(1L, articleId, user, "subcomment2")));
        // when
        mongoTemplate.save(comment);
        // then
        Comment result = mongoTemplate.findOne(
                new Query().addCriteria(Criteria.where("articleId").is(articleId)), Comment.class);
        then(result.getReplies())
                .hasSize(2);
    }
    
    @Test
     void findByArticle_Should_ListComments() {
        // given
        Board board = Board.builder().cafe(new Cafe("testcafe", "testcafe")).name("board").build();
        User commenter = new User(2L, "commenter", "password");
        Article article = new Article(10L, board, user, "title", "content", new ArrayList<>(), 0, 0, now(), now());

        Comment comment1 = new Comment(article, commenter, "comment1");
        mongoTemplate.insert(comment1);
        Comment comment2 = new Comment(article, commenter, "comment2");
        mongoTemplate.insert(comment2);
        Comment comment3 = new Comment(article, commenter, "comment3");
        mongoTemplate.insert(comment3);
        Comment comment4 = new Comment(article, commenter, "comment4");
        mongoTemplate.insert(comment4);
        // when
        List<Comment> comments = commentRepository.findByArticleId(article.getId(),
                PageRequest.of(0, 5, Sort.by("id")));
        // then
        then(comments)
                .extracting("text")
                .containsExactly("comment1", "comment2", "comment3", "comment4");
    }
}
