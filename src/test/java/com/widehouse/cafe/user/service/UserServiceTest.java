package com.widehouse.cafe.user.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

/**
 * Created by kiel on 2017. 2. 15..
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService service;
    @Mock
    private CafeMemberRepository cafeMemberRepository;

    @BeforeEach
    void setUp() {
        service = new UserService(cafeMemberRepository);
    }
    @Test
    void getCafesByMember_thenListCafes() {
        // given
        User user = new User(1L, "user", "password");
        Cafe cafe1 = new Cafe("url1", "name1");
        Cafe cafe2 = new Cafe("url2", "name2");
        Cafe cafe3 = new Cafe("url3", "name3");
        given(cafeMemberRepository.findCafeByMember(user, PageRequest.of(0, 3)))
                .willReturn(Arrays.asList(cafe1, cafe2, cafe3));
        // when
        List<Cafe> result = service.getCafesByUser(user, PageRequest.of(0, 3));
        // then
        then(result)
                .contains(cafe1, cafe2, cafe3);
    }
}
