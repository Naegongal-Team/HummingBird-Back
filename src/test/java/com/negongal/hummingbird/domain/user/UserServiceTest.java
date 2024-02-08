package com.negongal.hummingbird.domain.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
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
import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.MemberStatus;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.global.error.exception.NotExistException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
 	private UserService userService;

	@Mock
 	private UserRepository userRepository;

	@Test
	@DisplayName("사용자 닉네임 추가")
	void addNickname() {
		User user = User.builder().build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		UserDto userDto = UserDto.builder().nickname("user1").build();
		userService.addUserNicknameAndImage(userFakeId, userDto, null);

		assertThat(user.getNickname()).isEqualTo("user1");
	}


	@Test
	@DisplayName("사용자 정보가 조회된다.")
	void getUser() {
		User user = User.builder().nickname("user1").role(Role.USER).build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		UserDetailDto userDetailDto = userService.findUser(userFakeId);

		assertThat(userDetailDto.getNickname()).isEqualTo("user1");
		assertThat(userDetailDto.getRole()).isEqualTo("USER");
	}

	@Test
	@DisplayName("사용자 닉네임이 수정된다.")
	void modifyNickname() throws IOException {
		User user = User.builder().build();
		Long userFakeId = 1L;
		given(userRepository.findById(userFakeId)).willReturn(java.util.Optional.ofNullable(user));
		UserDto userDto = UserDto.builder().nickname("user2").build();
		userService.modifyUserNicknameAndImage(userFakeId, userDto, null);

		assertThat(user.getNickname()).isEqualTo("user2");
	}

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

	@Test
	@DisplayName("닉네임 중복 여부를 판단한다.")
	void findByNickname() {
		User user = User.builder().nickname("user1").build();
		given(userRepository.findByNickname("user1")).willReturn(java.util.Optional.ofNullable(user));
		int result1 = userService.findByNickname("user1");
		int result2 = userService.findByNickname("user2");

		assertThat(result1).isEqualTo(0);
		assertThat(result2).isEqualTo(1);
	}

	@Test
	@DisplayName("회원이 조회되지 않으면 예외가 발생한다.")
	void findUserException() {
		Assertions.assertThrows(NotExistException.class, () -> userService.findUser(1329831L));
	}
}
