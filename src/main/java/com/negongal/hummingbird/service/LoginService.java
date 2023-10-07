package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository repository;

    @Transactional
    public User join(User user) {
        return repository.save(user);
    }
    @Transactional
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return repository.findUserByEmail(email);
    }

}
