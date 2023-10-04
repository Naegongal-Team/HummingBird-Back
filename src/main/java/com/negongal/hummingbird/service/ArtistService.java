package com.negongal.hummingbird.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArtistService {

    private final SpotifyApi spotifyApi;

    /*
    인기순으로 아티스트 30명 조회
     */
    @Transactional
    public GetUsersTopArtistsRequest getArtists() {
        GetUsersTopArtistsRequest request = spotifyApi.getUsersTopArtists()
                .limit(30)
                .build();
        return request;
    }
}
