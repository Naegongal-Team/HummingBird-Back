package com.negongal.hummingbird.domain.user.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByOauth2IdAndProvider(String oauthId, String provider);

	Optional<User> findByNickname(String nickname);

	boolean existsByNickname(String nickname);

	List<User> findAllByStatusIs(UserStatus userStatus);

	Optional<User> findByOauth2Id(String oauth2Id);
}
