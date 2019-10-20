package com.widehouse.cafe.cafe.service;

import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static com.widehouse.cafe.cafe.entity.CafeVisibility.PUBLIC;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.BoardRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.entity.CategoryRepository;
import com.widehouse.cafe.common.exception.BoardNotExistsException;
import com.widehouse.cafe.common.exception.CafeNotFoundException;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

/**
 * Created by kiel on 2017. 2. 11..
 */
@ExtendWith(MockitoExtension.class)
class CafeServiceTest {
    private CafeService service;

    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CafeMemberRepository cafeMemberRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private Cafe cafe;

    @BeforeEach
    void setUp() {
        service = new CafeService(cafeRepository, cafeMemberRepository, boardRepository, categoryRepository);

        category = new Category(1, "category", 1, now());
        cafe = new Cafe("testurl", "testname", "desc", PUBLIC, category);
    }

    @Test
    void createCafe_Should_CreateCafe_and_AssignCafeManager() {
        // given
        User user = new User(1L, "user", "password");
        given(categoryRepository.findById(anyInt()))
                .willReturn(Optional.of(category));
        given(cafeRepository.save(any(Cafe.class)))
                .willReturn(new Cafe("testurl", "testname", "desc", PUBLIC, category));
        // when
        Cafe cafe = service.createCafe(user, "testurl", "testname", "desc", PUBLIC, category.getId());
        // then
        then(cafe)
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", PUBLIC)
                .hasFieldOrPropertyWithValue("category", category);
        verify(cafeRepository, times(2)).save(any(Cafe.class));
        verify(cafeMemberRepository).save(any(CafeMember.class));
        verify(boardRepository, times(8)).save(any(Board.class));
    }

    @Test
    void addBoard_without_listOrder_should_append_board_with_last_order() {
        // given
        Board board1 = Board.builder().cafe(cafe).name("board1").listOrder(1).build();
        Board board2 = Board.builder().cafe(cafe).name("board2").listOrder(3).build();
        List<Board> boards = Arrays.asList(board1, board2);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(boards);
        // when
        service.addBoard(cafe, "new test board");
        // then
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void removeBoard_should_remove_board() {
        // given
        Board board = Board.builder().cafe(cafe).name("board1").build();
        // when
        service.removeBoard(cafe, board);
        // then
        verify(boardRepository).delete(board);
    }

    @Test
    void changeBoard_should_change_board() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2));
        // when
        board1.update("update board1", 1);
        service.updateBoard(cafe, board1);
        // then
        then(boardRepository.findAllByCafe(cafe, Sort.by("listOrder")))
                .filteredOn("name", "update board1")
                .containsOnly(board1);
        verify(boardRepository).saveAll(anyList());
    }

    @Test
    void changeBoard_not_contained_board_do_nothing() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2));
        // when
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        service.updateBoard(cafe, board3);
        // then
        verify(boardRepository, times(0)).save(board3);
    }

    @Test
    void changeBoard_Should_changeBoardsListOrder() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2, board3));
        // when
        board1.update("update board1", 4);
        service.updateBoard(cafe, board1);
        // then
        verify(boardRepository).saveAll(Arrays.asList(board1, board2, board3));
    }

    @Test
    void listBoards_Should_ReturnListOfBoard() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        given(boardRepository.findAllByCafe(cafe, Sort.by("listOrder")))
                .willReturn(Arrays.asList(board1, board2, board3));
        // when
        List<Board> boards = service.listBoard(cafe);
        // then
        then(boards)
                .hasSize(3)
                .containsExactly(board1, board2, board3);
    }

    @Test
    void getCafe_WhenCafeId_Should_CafeInfo() {
        given(cafeRepository.findById(1L))
                .willReturn(Optional.of(new Cafe("testurl", "testname")));

        Cafe result = service.getCafe(1L);

        then(result)
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname");
    }

    @Test
    void getCafe_WhenCafeUrl_Should_CafeInfo() {
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(cafe);

        Cafe result = service.getCafe("testurl");

        then(result)
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname");
    }

    @Test
    void getCafe_withNotExistCafe_Should_ThrowCafeNotFoundException() {
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(null);

        thenThrownBy(() -> service.getCafe("testurl"))
                .isInstanceOf(CafeNotFoundException.class);
    }

    @Test
    void getBoard_thenReturnBoard() {
        Board board = new Board(1L, cafe, "board", LIST, 1);
        given(boardRepository.findById(1L))
                .willReturn(Optional.of(board));

        Board result = service.getBoard(1L);

        then(result)
                .isEqualTo(board);
    }

    @Test
    void getBoard_withNotExistBoard_thenRaiseBoardNotExistsExcetpion() {
        given(boardRepository.findById(1L))
                .willReturn(Optional.empty());

        thenThrownBy(() -> service.getBoard(1L))
                .isInstanceOf(BoardNotExistsException.class);
    }

    @Test
    void increaseCommentCount() {
        // given
        Cafe cafe = new Cafe(10L, "cafeurl", "cafename");
        long commentCount = cafe.getData().getCommentCount();
        // when
        service.increaseCommentCount(cafe);
        // then
        then(cafe.getData().getCommentCount())
                .isEqualTo(commentCount + 1);
        verify(cafeRepository).save(cafe);
    }

    @Test
    void decreaseCommentCount() {
        // given
        Cafe cafe = new Cafe(10L, "cafeurl", "cafename");
        for (int i = 0; i < 10; i++) {
            cafe.getData().increaseCommentCount();
        }
        long commentCount = cafe.getData().getCommentCount();
        // when
        service.decreaseCommentCount(cafe);
        // then
        then(cafe.getData().getCommentCount())
                .isEqualTo(commentCount - 1);
        verify(cafeRepository).save(cafe);
    }
}