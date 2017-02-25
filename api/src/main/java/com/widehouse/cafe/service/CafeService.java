package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafemember.CafeMemberRole;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.projection.CafeSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Service
public class CafeService {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    @Transactional
    public Cafe createCafe(Member member, String url, String name, String description,
                           CafeVisibility visibility, Category category) {
        Cafe cafe = new Cafe(url, name, description, visibility, category);
        cafeRepository.save(cafe);

        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafeMemberRepository.save(cafeMember);

        return cafe;
    }

    public List<CafeSummary> getCafeByCategory(Long categoryId, Pageable pageable) {
        return cafeRepository.findByCategoryId(categoryId, pageable);
    }

    public void addBoard(Cafe cafe, String boardName, int listOrder) {
        cafe.getBoards().add(new Board(cafe, boardName, listOrder));
        cafe.getBoards().sort(Comparator.comparing(Board::getListOrder));
        cafeRepository.save(cafe);
    }

    public void addBoard(Cafe cafe, String boardName) {
        int lastOrder = cafe.getBoards().stream()
                .sorted(Comparator.comparing(Board::getListOrder))
                .mapToInt(Board::getListOrder)
                .reduce((a, b) -> b)
                .orElse(0);
        addBoard(cafe, boardName, lastOrder + 1);
    }

    public void removeBoard(Cafe cafe, Board board) {
        cafe.getBoards().remove(board);
        cafe.getBoards().sort(Comparator.comparing(Board::getListOrder));
        cafeRepository.save(cafe);
    }

    public void updateBoard(Cafe cafe, Board board) {
        Optional<Board> boardOptional = cafe.getBoards().stream()
                .filter(o -> o.getId() == board.getId())
                .findFirst();
        if (boardOptional.isPresent()) {
            Board oldBoard = boardOptional.get();
            int index = cafe.getBoards().indexOf(oldBoard);
            cafe.getBoards().set(index, board);

            cafe.getBoards().sort(Comparator.comparing(Board::getListOrder));
            cafeRepository.save(cafe);
        }
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
        return cafeRepository.findByUrl(cafeUrl);
    }

}
