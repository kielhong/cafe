package com.widehouse.cafe.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kiel on 2017. 2. 15..
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
