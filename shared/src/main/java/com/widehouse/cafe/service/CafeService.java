package com.widehouse.cafe.service;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeNotFoundException;
import com.widehouse.cafe.projection.CafeProjection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

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
        Category category = categoryRepository.findOne(categoryId);
        Cafe cafe = cafeRepository.save(new Cafe(url, name, description, visibility, category));
        log.debug("cafe : {}", cafe);
        log.debug("member : {}", member);
        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafeMemberRepository.save(cafeMember);

        for (int i = 0; i < 4; i++) {
            boardRepository.save(new Board(cafe, "일반 게시판" + i, (i+1)));
        }

        return cafe;
    }

    public List<CafeProjection> getCafeByCategory(Long categoryId, Pageable pageable) {
        return cafeRepository.findByCategoryId(categoryId, pageable);
    }

    public void addBoard(Cafe cafe, String boardName, int listOrder) {
        Board board = new Board(cafe, boardName, listOrder);
        boardRepository.save(board);
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

            boardRepository.save(boards);
        }
    }

    public List<Board> listBoard(Cafe cafe) {
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(ASC, "listOrder"));

        return boards;
    }

    /**
     * get {@link Cafe} by cafe id
     * @param cafeId cafe id
     * @return Cafe Info
     */
    public Cafe getCafe(Long cafeId) {
        return cafeRepository.findOne(cafeId);
    }

    /**
     * get {@link Cafe} by cafe url
     * @param cafeUrl cafe url
     * @return Cafe Info
     */
    public Cafe getCafe(String cafeUrl) {
        Cafe cafe = cafeRepository.findByUrl(cafeUrl);
        if (cafe == null) {
            throw new CafeNotFoundException();
        }

        return cafe;
    }

}
