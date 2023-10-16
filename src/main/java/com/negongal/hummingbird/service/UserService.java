package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.UserUpdateDto;
import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void updateUserNickname(Long id, UserUpdateDto updateParam) {
        User findUser = userRepository.findById(id).orElseThrow();
        findUser.updateNickname(updateParam.getNickname());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByUuid(UUID uuid) {
        return userRepository.findUserByUuid(uuid).orElseThrow();
    }
}
