package com.negongal.hummingbird.domain.user.application;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.UserStatus;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.negongal.hummingbird.global.error.ErrorCode.USER_NOT_EXIST;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;

	public User getById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
	}

	public boolean existByNickname(String nickname) {
		return userRepository.existsByNickname(nickname);
	}

	@Transactional
	public void deleteUser(Long userId) {
		User findUser = getById(userId);
		findUser.updateInactiveDate();
		findUser.updateStatus(UserStatus.INACTIVE);
	}

	@Transactional
	public void activateUser(Long userId) {
		User findUser = getById(userId);
		findUser.updateStatus(UserStatus.ACTIVE);
	}

	@Transactional
	public void updateRefreshToken(Long id, String refreshToken) {
		User user = getById(id);
		user.updateRefreshToken(refreshToken);
	}

}
