package com.negongal.hummingbird.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@PropertySource("classpath:config.properties")
@Configuration
public class SpotifyConfig {

    @Value("${spotifyApi.client-id}")
    private static String clientId;

    @Value("${spotifyApi.client-secret}")
    private static String clientSecret;

    private static SpotifyApi spotifyApi;

    @PostConstruct
    public void init() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
        setAccessToken();
    }

    public static void setAccessToken() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            log.info("Expires in: {}", clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: {}", e.getMessage());

        }
    }

    @Bean
    public SpotifyApi spotifyApi() {
        return spotifyApi;
    }
}
