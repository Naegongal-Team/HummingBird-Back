package com.negongal.hummingbird.domain.artist.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, String> {
    
    @Query("SELECT a FROM Artist a LEFT JOIN ArtistHeart l ON a.id = l.artist.id GROUP BY a.id ORDER BY COUNT(l.id) DESC, a.name ASC")
    Page<Artist> findAll(Pageable pageable);

    List<Artist> findByNameContainingOrderByName(String name);

    Optional<Artist> findByName(String name);
}
