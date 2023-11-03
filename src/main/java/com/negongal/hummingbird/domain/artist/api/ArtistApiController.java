package com.negongal.hummingbird.domain.artist.api;

import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.wrapper.spotify.exceptions.detailed.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistApiController {

    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<HashMap<String, Page<ArtistDto>>> artistsList(Pageable pageable) {
        Page<ArtistDto> artistList = artistService.findArtists(pageable);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("artist_list", artistList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search/{artistName}")
    public ResponseEntity<List<ArtistSearchDto>> artistByNameList(@PathVariable String artistName) {
        List<ArtistSearchDto> artistSearchList = artistService.findArtistByName(artistName);
        return new ResponseEntity<>(artistSearchList, HttpStatus.OK);
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDetailDto> artistDetails(@PathVariable String artistId) throws NotFoundException {
        ArtistDetailDto artist = artistService.findArtist(artistId);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }



}
