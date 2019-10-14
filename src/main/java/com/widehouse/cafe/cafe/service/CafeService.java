package com.widehouse.cafe.cafe.service;

import static com.widehouse.cafe.article.entity.BoardType.BEST;
import static com.widehouse.cafe.article.entity.BoardType.BOOK;
import static com.widehouse.cafe.article.entity.BoardType.CALENDAR;
import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static com.widehouse.cafe.article.entity.BoardType.TAG;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.BoardRepository;
import com.widehouse.cafe.article.entity.BoardType;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeMemberRole;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.CafeVisibility;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.cafe.entity.CategoryRepository;
import com.widehouse.cafe.common.exception.BoardNotExistsException;
import com.widehouse.cafe.common.exception.CafeNotFoundException;
import com.widehouse.cafe.user.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kiel on 2017. 2. 11..
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CafeService {
    private final CafeRepository cafeRepository;
    private final CafeMemberRepository cafeMemberRepository;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    private static final String BOARD_LIST_ORDER = "listOrder";

    /**
     * Create a Cafe.
     * @param member member who creates cafe. He/She will be cafe manager
     * @param url url of cafe
     * @param name name of cafe
     * @param description description about cafe
     * @param visibility PUBLIC or PRIVATE
     * @param categoryId category of cafe
     * @return create {@link Cafe}
     */
    @Transactional
    public Cafe createCafe(User member, String url, String name, String description,
                           CafeVisibility visibility, Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        Cafe cafe = cafeRepository.save(new Cafe(url, name, description, visibility, category));
        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafeMemberRepository.save(cafeMember);
        cafe.getData().increaseCafeMemberCount();
        cafeRepository.save(cafe);

        addSpecialBoard(cafe);
        addNormalBoard(cafe, 4, 4);

        return cafe;
    }

    public void addBoard(Cafe cafe, String boardName, BoardType type, int listOrder) {
        Board board = Board.builder().cafe(cafe).name(boardName).type(type).listOrder(listOrder).build();
        boardRepository.save(board);
    }

    public void addBoard(Cafe cafe, String boardName, int listOrder) {
        addBoard(cafe, boardName, LIST, listOrder);
    }

    /**
     * add Board.
     * @param cafe cafe which add board
     * @param boardName added board name
     */
    public void addBoard(Cafe cafe, String boardName) {
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(DESC, BOARD_LIST_ORDER));
        int lastOrder = boards.stream()
                .sorted(Comparator.comparing(Board::getListOrder))
                .mapToInt(Board::getListOrder)
                .reduce((a, b) -> b)
                .orElse(0);
        addBoard(cafe, boardName, lastOrder + 1);
    }

    private void addSpecialBoard(Cafe cafe) {
        addBoard(cafe, "카페태그보기",TAG, 1);
        addBoard(cafe, "베스트게시물",BEST, 2);
        addBoard(cafe, "카페 캘린더",CALENDAR, 3);
        addBoard(cafe, "카페북 책꽂이",BOOK, 4);
    }

    private void addNormalBoard(Cafe cafe, int startOrder, int length) {
        for (int i = startOrder; i < startOrder + length; i++) {
            addBoard(cafe, "일반 게시판" + i, (i + 1));
        }
    }

    public void removeBoard(Cafe cafe, Board board) {
        boardRepository.delete(board);
    }

    public void updateBoard(Cafe cafe, Board board) {
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(ASC, BOARD_LIST_ORDER));
        Optional<Board> boardOptional = boards.stream()
                .filter(o -> o.getId() == board.getId())
                .findFirst();
        if (boardOptional.isPresent()) {
            Board oldBoard = boardOptional.get();
            int index = boards.indexOf(oldBoard);
            boards.set(index, board);

            boardRepository.saveAll(boards);
        }
    }

    public List<Board> listBoard(Cafe cafe) {
        return boardRepository.findAllByCafe(cafe, new Sort(ASC, BOARD_LIST_ORDER));
    }

    /**
     * get {@link Cafe} by cafe id.
     *
     * @param cafeId cafe id
     * @return Cafe Info
     */
    public Cafe getCafe(Long cafeId) {
        return cafeRepository.findById(cafeId).get();
    }

    /**
     * get {@link Cafe} by cafe url.
     *
     * @param url cafe url
     * @return Cafe Info
     */
    @Transactional
    public Cafe getCafe(String url) {
        Cafe cafe = cafeRepository.findByUrl(url);
        if (cafe == null) {
            throw new CafeNotFoundException();
        }

        return cafe;
    }

    @Transactional
    public Board getBoard(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (boardOptional.isPresent()) {
            return boardOptional.get();
        } else {
            throw new BoardNotExistsException();
        }

    }

}
