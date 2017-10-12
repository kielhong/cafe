package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeNotFoundException;
import com.widehouse.cafe.projection.CafeProjection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 11..
 */
@RunWith(SpringRunner.class)
@Import(CafeService.class)
@Slf4j
public class CafeServiceTest {
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private BoardRepository boardRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private Cafe mockCafe;
    @Autowired
    private CafeService cafeService;

    @Mock
    private CafeProjection cafeMock1;
    @Mock
    private CafeProjection cafeMock2;
    @Mock
    private CafeProjection cafeMock3;
    @Mock
    private CafeProjection cafeMock4;

    private Member member;
    private Category category;
    private Cafe cafe;


    @Before
    public void setUp() {
        member = new Member("user");
        category = new Category(1L, "category");
        cafe = new Cafe("testurl", "testname", "desc", PUBLIC, category);
    }

    @Test
    public void createCafe_Should_CreateCafe_and_AssignCafeManager() {
        // given
        given(categoryRepository.findById(category.getId()).get())
                .willReturn(category);
        given(cafeRepository.save(new Cafe("testurl", "testname", "desc", PUBLIC, any(Category.class))))
                .willReturn(cafe);
        // When
        Cafe cafe = cafeService.createCafe(member, "testurl", "testname", "desc", PUBLIC, category.getId());
        // Then
        assertThat(cafe)
                .isNotNull()
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", PUBLIC)
                .hasFieldOrPropertyWithValue("category", category);
        verify(cafeRepository, times(2)).save(any(Cafe.class));
        verify(cafeMemberRepository).save(any(CafeMember.class));
        verify(boardRepository ,times(8)).save(any(Board.class));
    }

    @Test
    public void getCafesByCategory_should_return_cafes_by_category() {
        // given
        given(cafeRepository.findByCategoryId(category.getId(),
                new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(cafeMock4, cafeMock3, cafeMock2, cafeMock1));
        // when
        List<CafeProjection> cafes = cafeService.getCafeByCategory(category.getId(), new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));
        // then
        assertThat(cafes)
                .contains(cafeMock4, cafeMock3, cafeMock2, cafeMock1);
    }

    @Test
    public void addBoard_should_attach_board_sort() {
        // when
        cafeService.addBoard(cafe, "test board3", 2);
        // then
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    public void addBoard_without_listOrder_should_append_board_with_last_order() {
        // given
        List<Board> boards = Arrays.asList(new Board(cafe, "board1", 1), new Board(cafe, "board3", 3));
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(boards);
        // when
        cafeService.addBoard(cafe, "new test board");
        // then
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    public void removeBoard_should_remove_board() {
        // given
        List<Board> boards = Arrays.asList(new Board(cafe, "board1", 1), new Board(cafe, "board3", 3), new Board(cafe, "board3", 4), new Board(cafe, "board4", 2));
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(boards);
        Board board = boards.get(1);
        // when
        cafeService.removeBoard(cafe, board);
        // then
        verify(boardRepository).delete(board);
    }

    @Test
    public void changeBoard_should_change_board() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2));
        // when
        board1.update("update board1", 1);
        cafeService.updateBoard(cafe, board1);
        // then
        assertThat(boardRepository.findAllByCafe(cafe, new Sort(Sort.Direction.ASC, "listOrder")))
                .filteredOn("name", "update board1")
                .containsOnly(board1);
        verify(boardRepository).saveAll(anyListOf(Board.class));
    }

    @Test
    public void changeBoard_not_contained_board_do_nothing() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2));
        // when
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        cafeService.updateBoard(cafe, board3);
        // then
        verify(boardRepository, times(0)).save(board3);
    }

    @Test
    public void changeBoard_Should_changeBoardsListOrder() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        given(boardRepository.findAllByCafe(eq(cafe), any(Sort.class)))
                .willReturn(Arrays.asList(board1, board2, board3));
        // when
        board1.update("update board1", 4);
        cafeService.updateBoard(cafe, board1);
        // then
        verify(boardRepository).saveAll(Arrays.asList(board1, board2, board3));
    }

    @Test
    public void listBoards_Should_ReturnListOfBoard() {
        // given
        Board board1 = new Board(1L, cafe, "board1", LIST, 1);
        Board board2 = new Board(2L, cafe, "board2", LIST, 2);
        Board board3 = new Board(3L, cafe, "new board", LIST, 3);
        given(boardRepository.findAllByCafe(cafe, new Sort(ASC, "listOrder")))
                .willReturn(Arrays.asList(board1, board2, board3));
        // when
        List<Board> boards = cafeService.listBoard(cafe);
        // then
        then(boards)
                .hasSize(3)
                .containsExactly(board1, board2, board3);
    }

    @Test
    public void getCafe_WhenCafeId_Should_CafeInfo() {
        // given
        given(cafeRepository.findById(1L).get())
                .willReturn(new Cafe("testurl", "testname"));
        // when
        Cafe cafe = cafeService.getCafe(1L);
        // then
        assertThat(cafe)
                .isNotNull()
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname");

    }

    @Test
    public void getCafe_WhenCafeUrl_Should_CafeInfo() {
        // given
        Cafe givenCafe = new Cafe(1L, "testurl", "testname");
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(givenCafe);
        // when
        Cafe cafe = cafeService.getCafe("testurl");
        // then
        assertThat(cafe)
                .isNotNull()
                .hasFieldOrPropertyWithValue("url", "testurl")
                .hasFieldOrPropertyWithValue("name", "testname");
    }

    @Test
    public void getCafe_WhenNotExistCafe_Should_ThrowCafeNotFoundException() {
        // given
        given(cafeRepository.findByUrl("testurl"))
                .willReturn(null);
        // when
        assertThatThrownBy(() -> cafeService.getCafe("testurl"))
                .isInstanceOf(CafeNotFoundException.class);
    }
}
