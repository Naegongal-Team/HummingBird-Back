//package com.negongal.hummingbird.domain.user;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.BDDMockito.*;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.negongal.hummingbird.domain.user.application.UserInfoService;
//import com.negongal.hummingbird.domain.user.application.UserService;
//import com.negongal.hummingbird.domain.user.dao.UserRepository;
//import com.negongal.hummingbird.domain.user.domain.Role;
//import com.negongal.hummingbird.domain.user.domain.User;
//import com.negongal.hummingbird.global.error.ErrorCode;
//import com.negongal.hummingbird.global.error.exception.NotExistException;
//
//@ExtendWith(MockitoExtension.class)
//public class UserInfoServiceTest {
//
//	@InjectMocks
//	private UserInfoService userInfoService;
//
//	@Mock
//	private UserService userService;
//
//	@Test
//	@DisplayName("사용자 닉네임 추가")
//	void addNickname() {
//		User user = User.builder().build();
//		Long userFakeId = 1L;
//		given(userService.getById(userFakeId)).willReturn(user);
//		UpdateNicknameRequest request = new UpdateNicknameRequest("user1");
//		userInfoService.updateUserNickname(userFakeId, request);
//
//		assertThat(user.getNickname()).isEqualTo("user1");
//	}
//
//	@Test
//	@DisplayName("사용자 정보가 조회된다.")
//	void getUser() {
//		User user = User.builder().nickname("user1").role(Role.USER).build();
//		Long userFakeId = 1L;
//		given(userService.getById(userFakeId)).willReturn(user);
//		UserDetailDto response = userInfoService.getUser(userFakeId);
//
//		assertThat(response.getNickname()).isEqualTo("user1");
//		assertThat(response.getRole()).isEqualTo("USER");
//	}
//
//	@Test
//	@DisplayName("회원이 조회되지 않으면 예외가 발생한다.")
//	void findUserException() {
//		Long userFakeId = 1329831L;
//		given(userService.getById(userFakeId)).willThrow(new NotExistException(ErrorCode.USER_NOT_EXIST));
//		Assertions.assertThrows(NotExistException.class, () -> userInfoService.getUser(userFakeId));
//	}
//
//	@Test
//	@DisplayName("닉네임 중복 여부를 판단한다.")
//	void findByNickname() {
//		User user = User.builder().nickname("user1").build();
//		given(userInfoService.nicknameInUse(user.getNickname())).willReturn(true);
//		boolean result1 = userInfoService.nicknameInUse("user1");
//		boolean result2 = userInfoService.nicknameInUse("user2");
//
//		assertThat(result1).isEqualTo(true);
//		assertThat(result2).isEqualTo(false);
//	}
//}
