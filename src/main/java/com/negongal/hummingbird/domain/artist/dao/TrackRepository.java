package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}