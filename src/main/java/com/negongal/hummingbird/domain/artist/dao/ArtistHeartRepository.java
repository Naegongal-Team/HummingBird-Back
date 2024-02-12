package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import java.util.List;
import java.util.Optional;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistHeartRepository extends JpaRepository<ArtistHeart, Long> {


    @Query("SELECT ah.artist FROM ArtistHeart ah WHERE ah.user.id = :userId ORDER BY ah.artist.name ASC")
    Page<Artist> findArtistsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT ah FROM ArtistHeart ah WHERE ah.user.id = :userId AND ah.artist.id = :artistId")
    Optional<ArtistHeart> findByUserIdAndArtistId(@Param("userId")Long userId, @Param("artistId") String artistId);

    @Query("SELECT ah FROM ArtistHeart ah WHERE ah.artist.name = :artistName AND ah.isAlarmed = true")
    List<ArtistHeart> findByArtistIdAndIsAlarmed(@Param("artistName")String artistName);
}
