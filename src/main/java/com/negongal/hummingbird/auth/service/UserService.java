package com.negongal.hummingbird.auth.service;

import com.negongal.hummingbird.auth.dto.UserUpdateDto;
import com.negongal.hummingbird.auth.domain.User;
import com.negongal.hummingbird.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
