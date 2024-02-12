package com.negongal.hummingbird.domain.user;

import static org.assertj.core.api.Assertions.*;

import com.negongal.hummingbird.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.config.QueryDSLConfig;
import com.negongal.hummingbird.global.error.ErrorCode;
import com.negongal.hummingbird.global.error.exception.NotExistException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDSLConfig.class, JpaConfig.class})
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	User user;

	@BeforeEach
	void beforeEach() {
		user = User.builder()
			.oauth2Id("111")
			.provider("kakao")
			.nickname("user1")
			.role(Role.USER)
			.build();
		userRepository.save(user);
	}

	@Test
	@DisplayName("회원 저장")
	void saveUser() {
		User savedUser = userRepository.save(user);
		assertThat(savedUser).isEqualTo(user);
	}

	@Test
	@DisplayName("회원 ID로 조회")
	void findById() {
		User findUser = userRepository.findById(user.getUserId())
			.orElseThrow(() -> new NotExistException(ErrorCode.USER_NOT_EXIST));
		assertThat(findUser).isEqualTo(user);
	}

	@Test
	@DisplayName("Oauth2ID와 provider로 회원 조회")
	void findByOauth2IdAndProvider() {
		User findUser = userRepository.findByOauth2IdAndProvider("111", "kakao")
			.orElseThrow(() -> new NotExistException(ErrorCode.USER_NOT_EXIST));
		assertThat(findUser).isEqualTo(user);
	}

	@Test
	@DisplayName("nickname으로 회원 조회")
	void findByNickname() {
		User findUser = userRepository.findByNickname("user1")
			.orElseThrow(() -> new NotExistException(ErrorCode.USER_NOT_EXIST));
		assertThat(findUser).isEqualTo(user);
	}
}
