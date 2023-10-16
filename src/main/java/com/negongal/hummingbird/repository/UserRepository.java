package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmailAndProvider(String email, String provider);

    public Optional<User> findUserByUuid(UUID uuid);
}
