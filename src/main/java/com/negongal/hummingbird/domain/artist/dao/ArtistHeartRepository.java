package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistHeartRepository extends JpaRepository<ArtistHeart, Long> {
}
