package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

    Page<Artist> findAll(Pageable pageable);

    List<Artist> findAllByNameStartingWithOrderByNameAsc(String name);

    Optional<Artist> findByName(String name);
}

