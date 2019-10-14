package com.widehouse.cafe.user.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.entity.UserRepository;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UserDetailsServiceTest {
    private UserDetailsServiceImpl service;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_thenReturnUser() {
        // given
        User user = new User("user", "password");
        given(userRepository.findByUsername("user"))
                .willReturn(Optional.of(user));
        // when
        UserDetails result = service.loadUserByUsername("user");
        // then
        then(result)
                .isEqualTo(user)
                .hasFieldOrPropertyWithValue("enabled", true);
    }

    @Test
    void loadUserByUsername_withNotExistsMember_thenRaiseUsernameNotFoundException() {
        // given
        given(userRepository.findByUsername("user"))
                .willReturn(Optional.empty());
        // expect
        thenThrownBy(() -> service.loadUserByUsername("user"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
