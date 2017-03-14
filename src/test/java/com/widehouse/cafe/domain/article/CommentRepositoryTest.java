package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.mongodb.MongoClient;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.config.MongoConfiguration;
import com.widehouse.cafe.domain.member.Member;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    private MongoTemplate template;

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private static MongodExecutable _mongodExe;
    private static MongodProcess _mongod;

    private static MongoClient mongo;

//    private Cafe cafe;
//    private Board board;
//    private Article article;
//    private Member writer;
//    private Member commenter;

    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    @BeforeClass
    public static void beforeClass() throws Exception {
        _mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build());
        _mongod = _mongodExe.start();
        mongo = new MongoClient("localhost", 12345);
    }

    @Before
    public void setUp () throws Exception {
        template = new MongoTemplate(mongo, "test");
    }

    @Test
    public void save_Should_SaveComment() throws Exception {
        // given
        Comment comment = new Comment(1L, 1L, "comment");
        // when
        commentRepository.save(comment);
        // then
        int collectionSize = template.findAll(Comment.class).size();
        then(collectionSize)
                .isEqualTo(1);
    }

    @Test
    public void findByArticle_Should_ListComments() {
        // given
        Cafe cafe = new Cafe("testcafe", "testcafe");
        Board board = new Board(cafe, "board");
        Member writer = new Member(1L, "member");
        Member commenter = new Member(2L, "commenter");
        Article article = new Article(1L, board, writer, "title", "content", Arrays.asList(), 0, LocalDateTime.now(), LocalDateTime.now());

        Comment comment1 = new Comment(article, commenter, "comment1");
        template.insert(comment1);
        Comment comment2 = new Comment(article, commenter, "comment2");
        template.insert(comment2);
        Comment comment3 = new Comment(article, commenter, "comment3");
        template.insert(comment3);
        Comment comment4 = new Comment(article, commenter, "comment4");
        template.insert(comment4);
        // when
        List<Comment> comments = commentRepository.findByArticleId(article.getId(), new PageRequest(0, 5, new Sort(ASC, "id")));
        // then
        then(comments)
                .extracting("comment")
                .containsExactly("comment1", "comment2", "comment3", "comment4");
    }


    @AfterClass
    public static void afterClass() throws Exception {
        mongo.close();
        _mongod.stop();
        _mongodExe.stop();
    }
}
