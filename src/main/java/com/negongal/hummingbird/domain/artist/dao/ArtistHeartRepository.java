package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistHeartRepository extends JpaRepository<ArtistHeart, Long> {

    @Query("SELECT ah FROM ArtistHeart ah WHERE ah.user.userId = :userId AND ah.artist.id = :artistId")
    Optional<ArtistHeart> findByUserIdAndArtistId(@Param("userId")Long userId, @Param("artistId") String artistId);
}
