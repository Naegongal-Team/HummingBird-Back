package com.negongal.hummingbird.domain.artist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.artist.domain.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
