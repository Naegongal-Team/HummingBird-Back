package com.negongal.hummingbird.domain.user.domain;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;

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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String oauth2Id;

    @Column(length = 8)
    private String nickname;

    @Column(nullable = false)
    private String provider;

    @Column(length = 1000)
    private String profileImage;

    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'USER'")
    private Role role;

    private String refreshToken;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PerformanceHeart> performanceHeartList;

    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private LocalDate inactiveDate;

    @Builder
    public User(String oauth2Id, String nickname, String provider, Role role, MemberStatus status) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.nickname = nickname;
        this.role = role;
        this.status = status;

        performanceHeartList = new ArrayList<>();
    }

    public void updateNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void updateFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateInactiveDate() {
        this.inactiveDate = LocalDate.now();
    }

    public void activateStatus() {
        this.status = MemberStatus.ACTIVE;
    }

    public void updateStatus() {
        this.status = MemberStatus.INACTIVE;
    }
}
