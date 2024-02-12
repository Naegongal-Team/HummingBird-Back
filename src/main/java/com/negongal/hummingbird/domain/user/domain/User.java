package com.negongal.hummingbird.domain.user.domain;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

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
    private List<PerformanceHeart> performanceHeartList = new ArrayList<>();;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus status;

    private LocalDate inactiveDate;

    @Builder
    public User(String oauth2Id, String nickname, String provider, Role role, UserStatus status) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
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

    public void activateStatus() {
        this.status = UserStatus.ACTIVE;
    }

    public void updateStatus() {
        this.status = UserStatus.INACTIVE;
    }
}
