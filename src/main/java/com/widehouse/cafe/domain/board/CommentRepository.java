package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kiel on 2017. 2. 12..
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
}
