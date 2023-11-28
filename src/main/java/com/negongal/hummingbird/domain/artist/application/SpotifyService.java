package com.negongal.hummingbird.domain.artist.application;

import com.negongal.hummingbird.domain.artist.dao.*;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.global.config.SpotifyConfig;
import com.negongal.hummingbird.global.error.exception.AlreadyExistException;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import org.apache.hc.core5.http.ParseException;
import com.neovisionaries.i18n.CountryCode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.artists.*;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import static com.negongal.hummingbird.global.config.SpotifyConfig.spotifyApi;
import static com.negongal.hummingbird.global.error.ErrorCode.*;

@PropertySource("classpath:config.yml")
@DependsOn("spotifyConfig")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SpotifyService {

    private static final CountryCode countryCode = CountryCode.KR;

    private static final int ARTIST_GENRES_LIMIT = 3;

    private static final int SEARCH_ARTIST_LIMIT = 10;

    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    @Value("${spotifyArtists.ids.array}")
    private String[] artistIds;

    private GetSeveralArtistsRequest getSeveralArtistsRequest;

    /*
    SpotifyApi Artist 정보를 CustomArtist 객체로 변환 후 DB에 저장
    */
    @PostConstruct
    public void initArtists()
            throws IOException, ParseException, SpotifyWebApiException {
        getSeveralArtistsRequest = spotifyApi.getSeveralArtists(artistIds).build();
        Artist[] artists = getSeveralArtistsRequest.execute();
        Arrays.stream(artists).forEach(artist -> {
            com.negongal.hummingbird.domain.artist.domain.Artist customArtist = convertSpotifyToCustomArtist(artist);
            artistRepository.save(customArtist);
        });
        getSeveralArtistSpotifyTrack();
    }

    public List<ArtistSearchDto> searchArtists(String artistName)
            throws IOException, ParseException, SpotifyWebApiException {
        SpotifyConfig.setAccessToken();
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(artistName)
                .limit(SEARCH_ARTIST_LIMIT)
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
        SpotifyConfig.setAccessToken();
        checkPresentArtistInRepository(artistId);
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistId).build();
        Artist artist = getArtistRequest.execute();
        com.negongal.hummingbird.domain.artist.domain.Artist customArtist = convertSpotifyToCustomArtist(artist);
        artistRepository.save(customArtist);
        findTrackByArtist(artistId);
    }

    private void checkPresentArtistInRepository(String artistId) {
        Optional<com.negongal.hummingbird.domain.artist.domain.Artist> searchArtist = artistRepository.findById(
                artistId);
        if (searchArtist.isPresent()) {
            throw new AlreadyExistException(ARTIST_ALREADY_EXIST);
        }
    }

    private void getSeveralArtistSpotifyTrack() throws IOException, ParseException, SpotifyWebApiException {
        for (String artist : artistIds) {
            findTrackByArtist(artist);
        }
    }

    public void findTrackByArtist(String artist) throws IOException, SpotifyWebApiException, ParseException {
        GetArtistsTopTracksRequest getArtistsTopTracksRequest = spotifyApi
                .getArtistsTopTracks(artist, countryCode)
                .build();

        Stream<Track> tracks = Arrays.stream(getArtistsTopTracksRequest.execute())
                .limit(ARTIST_GENRES_LIMIT);
        convertSpotifyToCustomTrack(tracks, artist);
    }

    private void convertSpotifyToCustomTrack(Stream<Track> spotifyTracks, String artistId) {
        com.negongal.hummingbird.domain.artist.domain.Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));
        spotifyTracks.forEach(track -> {
            Image spotifyAlbumImage = Arrays.stream(track.getAlbum().getImages()).findFirst()
                    .orElseThrow(() -> new NotExistException(ALBUM_IMAGE_NOT_EXIST));
            String albumImageUrl = spotifyAlbumImage.getUrl();
            com.negongal.hummingbird.domain.artist.domain.Track customTrack = com.negongal.hummingbird.domain.artist.domain.Track.builder()
                    .albumName(track.getAlbum().getName())
                    .artist(artist)
                    .releaseDate(track.getAlbum().getReleaseDate())
                    .albumImage(albumImageUrl)
                    .name(track.getName())
                    .build();

            trackRepository.save(customTrack);
        });
    }

    private com.negongal.hummingbird.domain.artist.domain.Artist convertSpotifyToCustomArtist(Artist spotifyArtist) {
        String spotifyArtistId = spotifyArtist.getId();
        List<String> spotifyArtistGenre = new ArrayList<>(List.of(spotifyArtist.getGenres()));
        Image spotifyArtistImage = Arrays.stream(spotifyArtist.getImages())
                .findFirst()
                .orElseThrow(() -> new NotExistException(ARTIST_IMAGE_NOT_EXIST));
        String spotifyArtistUrl = spotifyArtistImage.getUrl();

        return com.negongal.hummingbird.domain.artist.domain.Artist.builder()
                .id(spotifyArtistId)
                .name(spotifyArtist.getName())
                .genreList(spotifyArtistGenre)
                .popularity(spotifyArtist.getPopularity())
                .image(spotifyArtistUrl)
                .build();
    }
}
