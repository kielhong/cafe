package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.cafe.Cafe;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 16..
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByCafe(Cafe cafe, Sort sort);
}
