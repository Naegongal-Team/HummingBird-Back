package com.negongal.hummingbird.domain.user.dao;

import com.negongal.hummingbird.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByOauth2IdAndProvider(String oauthId, String provider);

    public Optional<User> findByNickname(String nickname);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.refreshToken=:token WHERE u.userId=:id AND u.provider=:provider")
    void updateRefreshToken(@Param("id") Long id,
                            @Param("provider") String provider,
                            @Param("token") String token);

    @Query("SELECT u.refreshToken FROM User u WHERE u.userId=:id")
    String getRefreshTokenById(@Param("id") Long id);

}
