package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private CafeService cafeService;
    @Autowired
    private CommentService commentService;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member manager;
    private Member commenter;

    @Before
    public void setUp() {
        manager = new Member("manager");
        cafe = cafeService.createCafe(manager, "url", "name" , "", CafeVisibility.PUBLIC, new CafeCategory());
        board = new Board(cafe,"article");
        Member writer = new Member("writer");
        article = new Article(cafe, board, writer, "title", "content");

        commenter = new Member("commenter");
        cafeService.joinMember(cafe, commenter);
    }

    @Test
    public void writeComment_Should_CreateComment_IncreaseCafeCommentCount_IncreaseArticleCommentCount() {
        // Given
        String commentText = "comment";
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();

        // When
        Comment comment = commentService.writeComment(article, commenter, commentText);
        // Then
        verify(commentRepository).save(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", commentText);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount + 1);
        assertThat(article.getCommentCount())
                .isEqualTo(articleCommentCount + 1);

    }

    @Test
    public void writeComment_by_not_cafemember_thorws_NoAuthorityException() {
        // Given
        Member notCafeMember = new Member("commenter");
        // Then
        assertThatThrownBy(() -> commentService.writeComment(article, notCafeMember, "comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void modifyComment_should_update_comment() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.modifyComment(comment, commenter, "another comment");
        // then
        verify(commentRepository).save(comment);
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", "another comment");
        assertThat(comment.getUpdateDateTime())
                .isAfterOrEqualTo(comment.getCreateDateTime());
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCommentCount);
    }

    @Test
    public void modifyComment_by_not_commenter_throw_NoAuthorityException() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Member another = new Member("another member");
        cafeService.joinMember(cafe, another);
        // then
        assertThatThrownBy(() -> commentService.modifyComment(comment, another, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void DeleteCommentByCmmenter_Should_Success_DecreaseCafeCommentCount_DecreaseArticleCommentCount() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Mockito.reset(commentRepository);
        Mockito.reset(cafeRepository);
        Mockito.reset(articleRepository);
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        // when
        commentService.deleteComment(comment, commenter);
        // then
        verify(commentRepository).delete(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount - 1);
        assertThat(article.getCommentCount())
                .isEqualTo(articleCommentCount - 1);
    }

    @Test
    public void deleteComment_by_manager_should_success_and_decrease_commentCount() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.deleteComment(comment, manager);
        // then
        verify(commentRepository).delete(comment);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount - 1);
    }

    @Test
    public void deleteComment_by_nonmanager_nor_noncomment_throw_NoAuthorityException() {
        // given
        Member member1 = new Member();
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        // then
        assertThatThrownBy(() -> commentService.deleteComment(comment, member1))
                .isInstanceOf(NoAuthorityException.class);
        verify(commentRepository, times(0)).delete(comment);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount);
    }
}