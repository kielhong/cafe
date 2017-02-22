package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 16..
 */
@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
}
