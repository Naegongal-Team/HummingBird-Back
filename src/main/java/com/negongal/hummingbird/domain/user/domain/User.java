package com.negongal.hummingbird.domain.user.domain;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Table(name = "users") //h2에서 user가 예약어임
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String oauth2Id;
    private String nickname;
    private String provider;
    private String profileImage;
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PerformanceHeart> performanceHeartList;

    @Builder
    public User(String oauth2Id, String nickname, String provider, Role role) {
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.nickname = nickname;
        this.role = role;
    }

    public void updateNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public void updateFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
=======
    public void updateInactiveDate() {
        this.inactiveDate = LocalDateTime.now();
>>>>>>> e784eb0 (feat. 회원 탈퇴 기능 추가)
    }

=======
>>>>>>> a91a356 (feat: fcm 토큰 저장을 위해 User class 수정)
}
