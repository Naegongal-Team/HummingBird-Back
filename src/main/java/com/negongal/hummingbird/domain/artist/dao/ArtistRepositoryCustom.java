package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.QTuple;
import java.util.List;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistRepositoryCustom {

    Page<ArtistDto> findAllArtists(Long userId, Pageable pageable);
}
