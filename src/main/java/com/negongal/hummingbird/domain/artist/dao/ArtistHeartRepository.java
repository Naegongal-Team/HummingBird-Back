package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistHeartRepository extends JpaRepository<ArtistHeart, Long> {


    @Query("SELECT ah.artist FROM ArtistHeart ah WHERE ah.user.userId = :userId ORDER BY ah.artist.name ASC")
    Page<Artist> findArtistsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT ah FROM ArtistHeart ah WHERE ah.user.userId = :userId AND ah.artist.id = :artistId")
    ArtistHeart findByUserIdAndArtistId(@Param("userId")Long userId, @Param("artistId") String artistId);
}
