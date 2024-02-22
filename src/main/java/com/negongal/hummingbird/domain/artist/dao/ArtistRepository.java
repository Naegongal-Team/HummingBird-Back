package com.negongal.hummingbird.domain.artist.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.artist.domain.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String>, ArtistRepositoryCustom {

	Page<Artist> findAll(Pageable pageable);

	List<Artist> findByNameStartingWithOrderByNameAsc(String name);

	Optional<Artist> findByName(String name);
}

