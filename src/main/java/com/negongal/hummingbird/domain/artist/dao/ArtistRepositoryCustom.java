package com.negongal.hummingbird.domain.artist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;

@Repository
public interface ArtistRepositoryCustom {

	Page<ArtistDto> findLikedArtists(Long userId, Pageable pageable);
}
