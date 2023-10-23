package com.negongal.hummingbird.domain.user.repository;

import com.negongal.hummingbird.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByOauth2IdAndProvider(String oauthId, String provider);

    public Optional<User> findByOauth2Id(String oauthId);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.refreshToken=:token WHERE u.oauth2Id=:id AND u.provider=:provider")
    void updateRefreshToken(@Param("id") String id,
                            @Param("provider") String provider,
                            @Param("token") String token);

}
