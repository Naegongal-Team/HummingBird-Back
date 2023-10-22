package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.ArtistLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistLikeRepository extends JpaRepository<ArtistLike, Long> {
}
