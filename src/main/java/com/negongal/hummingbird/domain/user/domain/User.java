package com.negongal.hummingbird.domain.user.domain;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;
import com.negongal.hummingbird.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Getter
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String oauth2Id;

	@Column(length = 8)
	private String nickname;

	@NotNull
	private String provider;

	@Column(length = 1000)
	private String profileImage;

	private String fcmToken;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;

	private String refreshToken;

	@OneToMany(mappedBy = "user", orphanRemoval = true)
	private List<PerformanceHeart> performanceHeartList = new ArrayList<>();

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	private LocalDate inactiveDate;

	@Builder
	public User(String oauth2Id, String provider, Role role, UserStatus status) {
		this.oauth2Id = oauth2Id;
		this.provider = provider;
		this.role = role;
		this.status = status;
	}

	public static User createUser(Oauth2Attributes attributes) {
		return User.builder()
			.oauth2Id(attributes.getOauth2Id())
			.provider(attributes.getProvider())
			.role(Role.USER)
			.status(UserStatus.ACTIVE)
			.build();
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updatePhoto(String photoUrl) {
		this.profileImage = photoUrl;
	}

	public void updateFCMToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public void updateInactiveDate() {
		this.inactiveDate = LocalDate.now();
	}

	public void updateStatus(UserStatus status) {
		this.status = status;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public GrantedAuthority toGrantedAuthority() {
		return new SimpleGrantedAuthority(role.getAuthority());
	}
}
