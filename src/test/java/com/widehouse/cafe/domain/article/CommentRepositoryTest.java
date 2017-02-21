package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CommentRepository commentRepository;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member writer;
    private Member commenter;

    @Before
    public void setUp () {
        cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        board = new Board(cafe,"article");
        entityManager.persist(board);
        writer = new Member("writer");
        entityManager.persist(writer);
        article = new Article(board, writer, "title", "content");
        entityManager.persist(article);
        commenter = new Member("commenter");
        entityManager.persist(commenter);
    }

    @Test
    public void findByArticle_Should_ListComments() {
        // given
        Article article2 = new Article(board, writer, "title2", "content2");
        entityManager.persist(article2);
        Comment comment1 = new Comment(article, commenter, "comment1");
        entityManager.persist(comment1);
        Comment comment2 = new Comment(article, commenter, "comment2");
        entityManager.persist(comment2);
        Comment comment3 = new Comment(article, commenter, "comment3");
        entityManager.persist(comment3);
        Comment comment4 = new Comment(article, commenter, "comment4");
        entityManager.persist(comment4);
        Comment comment5 = new Comment(article2, commenter, "comment5");
        entityManager.persist(comment5);
        // when
        List<Comment> comments = commentRepository.findByArticle(article, new PageRequest(0, 5, new Sort(ASC, "id")));
        // then
        then(comments)
                .containsExactly(comment1, comment2, comment3, comment4);
    }

}
