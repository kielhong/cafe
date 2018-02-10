package com.widehouse.cafe.domain.cafe;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 16..
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByCafe(Cafe cafe, Sort sort);
}
