package com.negongal.hummingbird.domain.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.MemberStatus;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Test
	@DisplayName("사용자가 탈퇴한다.")
	void deleteUser() {
		User user = User.builder().build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		userService.deleteUser(1L);

		assertThat(user.getInactiveDate()).isEqualTo(LocalDate.now());
		assertThat(user.getStatus()).isEqualTo(MemberStatus.INACTIVE);
	}

	@Test
	@DisplayName("탈퇴한 사용자가 계정을 재활성화 한다.")
	void activateUser() {
		User user = User.builder().status(MemberStatus.INACTIVE).build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		userService.activateUser(userFakeId);

		assertThat(user.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}
}
