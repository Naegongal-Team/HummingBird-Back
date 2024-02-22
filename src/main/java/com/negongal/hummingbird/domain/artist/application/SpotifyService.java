package com.negongal.hummingbird.domain.artist.application;

import static com.negongal.hummingbird.global.config.SpotifyConfig.*;
import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.dao.GenreRepository;
import com.negongal.hummingbird.domain.artist.dao.TrackRepository;
import com.negongal.hummingbird.domain.artist.domain.Genre;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.global.config.SpotifyConfig;
import com.negongal.hummingbird.global.error.exception.AlreadyExistException;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import com.neovisionaries.i18n.CountryCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

@PropertySource("classpath:spotify.yml")
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

	private final GenreRepository genreRepository;

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

	public void saveArtist(String artistName) throws IOException, ParseException, SpotifyWebApiException {
		SpotifyConfig.setAccessToken();
		String artistId = searchArtists(artistName).get(0).getId();
		checkPresentArtistInRepository(artistId);
		GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistId).build();
		Artist artist = getArtistRequest.execute();
		com.negongal.hummingbird.domain.artist.domain.Artist customArtist = convertSpotifyToCustomArtist(artist);
		artistRepository.save(customArtist);
		findTrackByArtist(artistId);
		findGenreByArtist(artist);
	}

	private void checkPresentArtistInRepository(String artistId) {
		Optional<com.negongal.hummingbird.domain.artist.domain.Artist> searchArtist = artistRepository.findById(
			artistId);
		if (searchArtist.isPresent()) {
			throw new AlreadyExistException(ARTIST_ALREADY_EXIST);
		}
	}

	public void findTrackByArtist(String artistId) throws IOException, SpotifyWebApiException, ParseException {
		GetArtistsTopTracksRequest getArtistsTopTracksRequest = spotifyApi
			.getArtistsTopTracks(artistId, countryCode)
			.build();

		Stream<Track> tracks = Arrays.stream(getArtistsTopTracksRequest.execute())
			.limit(ARTIST_GENRES_LIMIT);
		convertSpotifyToCustomTrack(tracks, artistId);
	}

	private void findGenreByArtist(Artist artist) throws IOException, SpotifyWebApiException, ParseException {
		List<String> genres = List.of(artist.getGenres());
		convertSpotifyToCustomGenre(genres, artist.getId());
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

	private void convertSpotifyToCustomGenre(List<String> spotifyGenres, String artistId) {
		com.negongal.hummingbird.domain.artist.domain.Artist artist = artistRepository.findById(artistId)
			.orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));

		spotifyGenres.forEach(genre -> {
			Genre newGenre = Genre.builder()
				.name(genre)
				.artist(artist)
				.build();

			genreRepository.save(newGenre);
		});
	}

	private com.negongal.hummingbird.domain.artist.domain.Artist convertSpotifyToCustomArtist(Artist spotifyArtist) {
		String spotifyArtistId = spotifyArtist.getId();
		Image spotifyArtistImage = Arrays.stream(spotifyArtist.getImages())
			.findFirst()
			.orElseThrow(() -> new NotExistException(ARTIST_IMAGE_NOT_EXIST));
		String spotifyArtistUrl = spotifyArtistImage.getUrl();

		return com.negongal.hummingbird.domain.artist.domain.Artist.builder()
			.id(spotifyArtistId)
			.name(spotifyArtist.getName())
			.image(spotifyArtistUrl)
			.build();
	}
}
