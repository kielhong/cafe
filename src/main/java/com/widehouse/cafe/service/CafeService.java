package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeMember;
import com.widehouse.cafe.domain.cafe.CafeMemberRole;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Service
public class CafeService {
    private CafeRepository cafeRepository;

    @Autowired
    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public Cafe createCafe(Member member, String url, String name, String description,
                           CafeVisibility visibility, CafeCategory category) {
        Cafe cafe = new Cafe(url, name, description, visibility, category);

        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafe.addCafeMember(cafeMember);

        return cafe;
    }

    public CafeMember joinMember(Cafe cafe, Member member, CafeMemberRole role) {
        if (cafe.getCafeMembers().stream().noneMatch((x -> x.getMember().equals(member)))) {
            CafeMember cafeMember = new CafeMember(cafe, member, role);
            cafe.addCafeMember(cafeMember);

            return cafeMember;
        } else {
            throw new CafeMemberAlreadyExistsException();
        }
    }

    public CafeMember joinMember(Cafe cafe, Member member) {
        return joinMember(cafe, member, CafeMemberRole.MEMBER);
    }

    public List<Cafe> getCafeByCategory(Long categoryId, Pageable pageable) {
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
}
