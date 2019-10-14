package com.widehouse.cafe.user.entity;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Created by kiel on 2017. 3. 3..
 */
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_thenReturnUser() {
        User user = entityManager.persist(new User("foo", "password"));

        Optional<User> result = userRepository.findByUsername("foo");

        then(result)
                .isPresent()
                .hasValue(user);
    }
}