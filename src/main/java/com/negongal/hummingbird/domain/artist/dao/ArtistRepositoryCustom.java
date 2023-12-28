package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistRepositoryCustom {

    Page<ArtistDto> findLikedArtists(Long userId, Pageable pageable);
}
