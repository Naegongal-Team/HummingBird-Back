package com.negongal.hummingbird.domain.user.dao;

import com.negongal.hummingbird.domain.user.domain.UserStatus;
import com.negongal.hummingbird.domain.user.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByOauth2IdAndProvider(String oauthId, String provider);

	Optional<User> findByNickname(String nickname);

	boolean existsByNickname(String nickname);

	List<User> findAllByStatusIs(UserStatus userStatus);

	Optional<User> findByOauth2Id(String oauth2Id);
}
