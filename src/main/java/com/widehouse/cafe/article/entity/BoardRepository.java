package com.widehouse.cafe.article.entity;

import com.widehouse.cafe.cafe.entity.Cafe;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 16..
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByCafe(Cafe cafe, Sort sort);
}
