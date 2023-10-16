package com.negongal.hummingbird.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Table(name = "users") //h2에서 user가 예약어임
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
//    @Column(columnDefinition = "BINARY(16)")
    @Type(type="uuid-char")
    private UUID uuid;
    private String nickname;
    private String provider;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder //생성을 Builder 패턴으로 하기 위해서
    public User(String nickname, String provider, String email, Role role, UUID uuid) {
        this.provider = provider;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.uuid = UUID.randomUUID();
    }

    public User updateEmail(String email) {
        this.email = email;
        return this;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
