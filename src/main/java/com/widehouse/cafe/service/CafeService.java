package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.BoardType.BEST;
import static com.widehouse.cafe.domain.cafe.BoardType.BOOK;
import static com.widehouse.cafe.domain.cafe.BoardType.CALENDAR;
import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static com.widehouse.cafe.domain.cafe.BoardType.TAG;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.BoardType;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.BoardNotExistsException;
import com.widehouse.cafe.exception.CafeNotFoundException;
import com.widehouse.cafe.projection.CafeProjection;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Service
@Slf4j
public class CafeService {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Cafe createCafe(Member member, String url, String name, String description,
                           CafeVisibility visibility, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        Cafe cafe = cafeRepository.save(new Cafe(url, name, description, visibility, category));
        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafeMemberRepository.save(cafeMember);
        cafe.getStatistics().increaseCafeMemberCount();
        cafeRepository.save(cafe);

        addSpecialBoard(cafe);
        addNormalBoard(cafe, 4, 4);

        return cafe;
    }

    public List<Cafe> getCafeByCategory(Long categoryId, Pageable pageable) {
        return cafeRepository.findByCategoryId(categoryId, pageable);
    }

    public void addBoard(Cafe cafe, String boardName, BoardType type, int listOrder) {
        Board board = new Board(cafe, boardName, type, listOrder);
        boardRepository.save(board);
    }

    public void addBoard(Cafe cafe, String boardName, int listOrder) {
        addBoard(cafe, boardName, LIST, listOrder);
    }

    public void addBoard(Cafe cafe, String boardName) {
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(DESC, "listOrder"));
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
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(ASC, "listOrder"));
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
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(ASC, "listOrder"));

        return boards;
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
