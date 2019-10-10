package com.widehouse.cafe.comment.entity;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.mongodb.MongoClient;
import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.config.MongoConfiguration;
import com.widehouse.cafe.member.entity.Member;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by kiel on 2017. 2. 20..
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
@Slf4j
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    private MongoTemplate template;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private static MongodExecutable _mongodExe;
    private static MongodProcess _mongod;

    private static MongoClient mongo;
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    private Member member;

    @BeforeAll
    static void initAll() throws Exception {
        _mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build());
        _mongod = _mongodExe.start();
        mongo = new MongoClient("localhost", 12345);
    }

    @AfterAll
    static void tearDownAll() {
        mongo.close();
        _mongod.stop();
        _mongodExe.stop();
    }

    @BeforeEach
     void init() {
        template = new MongoTemplate(mongo, "test");
        template.dropCollection(Comment.class);

        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
    }

    @Test
     void saveCommentTest() {
        // given
        Comment comment = new Comment(1L, member, "comment");
        // when
        commentRepository.save(comment);
        // then
        int collectionSize = template.findAll(Comment.class).size();
        then(collectionSize)
                .isEqualTo(1);
    }

    @Test
     void saveReplyCommentsTest() {
        // given
        Comment comment = new Comment(1L, member, "comment");
        comment.getComments().addAll(
                Arrays.asList(
                    new Comment(1L, member, "subcomment1"),
                    new Comment(1L, member, "subcomment2")));
        // when
        template.save(comment);
        // then
        Comment result = template.findOne(new Query().addCriteria(Criteria.where("articleId").is(1L)), Comment.class);
        then(result.getComments())
                .hasSize(2);
    }


    @Test
     void findByArticle_Should_ListComments() {
        // given
        Cafe cafe = new Cafe("testcafe", "testcafe");
        Board board = new Board(cafe, "board");
        Member commenter = new Member(2L, "commenter", "password", "nickname", "comment@bar.com");
        Article article = new Article(1L, board, member, "title", "content", Collections.emptyList(), 0, now(), now());

        Comment comment1 = new Comment(article, commenter, "comment1");
        template.insert(comment1);
        Comment comment2 = new Comment(article, commenter, "comment2");
        template.insert(comment2);
        Comment comment3 = new Comment(article, commenter, "comment3");
        template.insert(comment3);
        Comment comment4 = new Comment(article, commenter, "comment4");
        template.insert(comment4);
        // when
        List<Comment> comments = commentRepository.findByArticleId(article.getId(),
                PageRequest.of(0, 5, new Sort(ASC, "id")));
        // then
        then(comments)
                .extracting("comment")
                .containsExactly("comment1", "comment2", "comment3", "comment4");
    }
}
