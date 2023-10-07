package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByUserNum(Long userNum);
    public Optional<User> findUserByEmail(String email);
}
