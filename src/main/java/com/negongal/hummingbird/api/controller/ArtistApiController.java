package com.negongal.hummingbird.api.controller;

import com.negongal.hummingbird.api.dto.ArtistDto;
import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.service.ArtistLikeService;
import com.negongal.hummingbird.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistApiController {

    private final ArtistService artistService;

    private final ArtistLikeService artistLikeService;

    @GetMapping
    public ResponseEntity<HashMap<String, Page<ArtistDto>>> getArtists(Pageable pageable) {
        Page<ArtistDto> artistList = artistService.getArtists(pageable).map(ArtistDto::of);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("artist_list", artistList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
