package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
