package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.dao.*;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.artists.*;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.negongal.hummingbird.global.config.SpotifyConfig.spotifyApi;

@PropertySource("classpath:config.properties")
@DependsOn("spotifyConfig")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SpotifyService {

    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    @Value("${spotifyArtists.ids.array}")
    private String[] artistIds;

    private GetSeveralArtistsRequest getSeveralArtistsRequest;

    private static final CountryCode countryCode = CountryCode.KR;

    /*
    SpotifyApi Artist 정보를 CustomArtist 객체로 변환 후 DB에 저장
    */
    @PostConstruct
    public void initArtists() throws IOException, ParseException, SpotifyWebApiException {
        getSeveralArtistsRequest = spotifyApi.getSeveralArtists(artistIds).build();
        Artist[] artists = getSeveralArtistsRequest.execute();
        Arrays.stream(artists).forEach(artist -> {
            com.negongal.hummingbird.domain.artist.domain.Artist customArtist = converSpotifyToCustomArtist(artist);
            artistRepository.save(customArtist);
        });
        getArtistSpotifyTrack();
    }

    public List<ArtistSearchDto> searchArtists(String artistName)
            throws IOException, ParseException, SpotifyWebApiException {
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(artistName)
                .limit(10)
                .build();
        Paging<Artist> artistPaging = searchArtistsRequest.execute();

        return Arrays.stream(artistPaging.getItems())
                .map(artist -> ArtistSearchDto.builder()
                        .id(artist.getId())
                        .name(artist.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveArtist(String artistId) throws IOException, ParseException, SpotifyWebApiException {
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistId).build();
        Artist artist = getArtistRequest.execute();
        com.negongal.hummingbird.domain.artist.domain.Artist customArtist = converSpotifyToCustomArtist(artist);
        artistRepository.save(customArtist);
    }

    private void getArtistSpotifyTrack() throws IOException, ParseException, SpotifyWebApiException {
        for (String artist : artistIds) {
            GetArtistsTopTracksRequest getArtistsTopTracksRequest = spotifyApi
                    .getArtistsTopTracks(artist, countryCode)
                    .build();

            Stream<Track> tracks = Arrays.stream(getArtistsTopTracksRequest.execute())
                    .limit(3);
            convertSpotifyToCustomTrack(tracks, artist);
        }
    }

    private void convertSpotifyToCustomTrack(Stream<Track> spotifyTracks, String artistId) {
        com.negongal.hummingbird.domain.artist.domain.Artist artist = artistRepository.findById(artistId)
                .orElseThrow(NoSuchElementException::new);

        spotifyTracks.forEach(track -> {
            com.negongal.hummingbird.domain.artist.domain.Track customTrack = com.negongal.hummingbird.domain.artist.domain.Track.builder()
                    .albumName(track.getAlbum().getName())
                    .artist(artist)
                    .releaseDate(track.getAlbum().getReleaseDate())
                    .name(track.getName())
                    .build();

            trackRepository.save(customTrack);
        });
    }

    private com.negongal.hummingbird.domain.artist.domain.Artist converSpotifyToCustomArtist(Artist spotifyArtist) {
        String spotifyArtistId = spotifyArtist.getId();
        List<String> spotifyArtistGenre = new ArrayList<>(List.of(spotifyArtist.getGenres()));
        Image spotifyArtistImage = Arrays.stream(spotifyArtist.getImages())
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        String spotifyArtistUrl = spotifyArtistImage.getUrl();

        return com.negongal.hummingbird.domain.artist.domain.Artist.builder()
                .id(spotifyArtistId)
                .name(spotifyArtist.getName())
                .genres(spotifyArtistGenre)
                .popularity(spotifyArtist.getPopularity())
                .image(spotifyArtistUrl)
                .build();
    }
}
