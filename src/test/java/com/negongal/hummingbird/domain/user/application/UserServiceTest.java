package com.negongal.hummingbird.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.UserStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE);
	}

	@Test
	@DisplayName("탈퇴한 사용자가 계정을 재활성화 한다.")
	void activateUser() {
		User user = User.builder().status(UserStatus.INACTIVE).build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		userService.activateUser(userFakeId);

		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}
