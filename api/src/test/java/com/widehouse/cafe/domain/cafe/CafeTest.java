package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 10..
 */
public class CafeTest {
    private Cafe cafe;

    @Before
    public void setUp() {
        cafe = new Cafe("testcafe", "testcafe");
    }

    @Test
    public void createCafe() {
        // Given
        Category category = new Category();
        // When
        Cafe cafe = new Cafe("cafeurl", "cafename", "desc", CafeVisibility.PUBLIC, category);
        // Then
        assertThat(cafe)
                .hasFieldOrPropertyWithValue("url", "cafeurl")
                .hasFieldOrPropertyWithValue("name", "cafename")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", CafeVisibility.PUBLIC)
                .hasFieldOrPropertyWithValue("category", category);
        assertThat(cafe.getCafeMembers())
                .hasSize(0);


    }

    @Test
    public void updateCafeInfo_should_change_cafeName_description_visibility() {
        // When
        Category category = new Category("category");
        cafe.updateInfo("new name", "new description", CafeVisibility.PRIVATE, category);
        // Then
        assertThat(cafe.getName())
                .isEqualTo("new name");
        assertThat(cafe.getDescription())
                .isEqualTo("new description");
        assertThat(cafe.getVisibility())
                .isEqualTo(CafeVisibility.PRIVATE);
        assertThat(cafe.getCategory())
                .isEqualTo(category);
    }


    @Test
    public void addCafeMember_increase_cafeMemberCount() {
        // Given
        Member member = new Member();
        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MEMBER);
        int beforeSize = cafe.getCafeMembers().size();
        // When
        cafe.addCafeMember(cafeMember);
        // Then
        assertThat(cafe.getCafeMembers())
                .hasSize(beforeSize + 1)
                .contains(cafeMember);
        assertThat(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(1);
    }

    @Test
    public void removeCafeMember_decrease_cafeMember() {
        // Given
        Member member1 = new Member();
        Member member2 = new Member();
        CafeMember cafeMember1 = new CafeMember(cafe, member1);
        CafeMember cafeMember2 = new CafeMember(cafe, member2);
        cafe.addCafeMember(cafeMember1);
        cafe.addCafeMember(cafeMember2);
        int beforeSize = cafe.getCafeMembers().size();
        // When
        cafe.removeCafeMember(cafeMember1);
        // Then
        assertThat(cafe.getCafeMembers())
                .doesNotContain(cafeMember1)
                .hasSize(beforeSize - 1);
        assertThat(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(1);
    }

    @Test
    public void isCafeMember_should_true_member_is_cafeMember() {
        // given
        Member member1 = new Member();
        CafeMember cafeMember1 = new CafeMember(cafe, member1);
        cafe.addCafeMember(cafeMember1);
        // when
        boolean result = cafe.isCafeMember(member1);
        // then
        assertThat(result)
                .isTrue();
    }

    @Test
    public void boards_list_by_listOrder() {
        // given
        Board board1 = new Board(cafe, "board1", 4);
        Board board2 = new Board(cafe, "board2", 3);
        Board board3 = new Board(cafe, "board3", 2);
        Board board4 = new Board(cafe, "board4", 1);
        cafe.getBoards().add(board1);
        cafe.getBoards().add(board2);
        cafe.getBoards().add(board3);
        cafe.getBoards().add(board4);
        // when
        List<Board> boards = cafe.getBoards();
        // then
        then(boards)
                .hasSize(4)
                .contains(board4, board3, board2, board1);
    }
}