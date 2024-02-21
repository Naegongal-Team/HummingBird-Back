package com.negongal.hummingbird.global.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

import java.io.IOException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Slf4j
@PropertySource("classpath:spotify.yml")
@Configuration
public class SpotifyConfig {

	private static String SPOTIFY_API_CLIENT_ID;

	private static String SPOTIFY_API_CLIENT_SECRET;

	public static SpotifyApi spotifyApi;

	@PostConstruct
	private void initSpotifyApi() throws IOException, ParseException, SpotifyWebApiException {
		spotifyApi = new SpotifyApi.Builder()
			.setClientId(SPOTIFY_API_CLIENT_ID)
			.setClientSecret(SPOTIFY_API_CLIENT_SECRET)
			.build();
		setAccessToken();
	}

	public static void setAccessToken() throws IOException, ParseException, SpotifyWebApiException {
		ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
		ClientCredentials clientCredentials = clientCredentialsRequest.execute();
		spotifyApi.setAccessToken(clientCredentials.getAccessToken());

		log.info("Expires in: {}", clientCredentials.getExpiresIn());
	}

	@Bean
	public SpotifyApi spotifyApi() {
		return spotifyApi;
	}

	@Value("${spotifyApi.client-id}")
	public void setClientId(String clientId) {
		SPOTIFY_API_CLIENT_ID = clientId;
	}

	@Value("${spotifyApi.client-secret}")
	public void setClientSecret(String clientSecret) {
		SPOTIFY_API_CLIENT_SECRET = clientSecret;
	}
}
