package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
