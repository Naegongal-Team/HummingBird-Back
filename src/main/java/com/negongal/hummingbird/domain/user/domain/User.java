package com.negongal.hummingbird.domain.user.domain;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Table(name = "users") //h2에서 user가 예약어임
@Getter
@SQLDelete(sql = "UPDATE user SET status='INACTIVE' WHERE userId = ?")
@Where(clause = "status = 'ACTIVE'")
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
    private Role role;

    private String refreshToken;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PerformanceHeart> performanceHeartList;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private LocalDateTime inactiveDate;

    @Builder
    public User(String oauth2Id, String nickname, String provider, Role role, MemberStatus status) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
    }

    public void updateNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
    public void updateFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    public void updateInactiveDate() {
        this.inactiveDate = LocalDateTime.now();
    }

    public void updateInactiveDate() {
        this.inactiveDate = LocalDateTime.now();
    }
}
