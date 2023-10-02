package com.negongal.hummingbird.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@PropertySource("classpath:config.properties")
@Component
public class SpotifyAuthorization {

    @Value("${spotifyApi.client-id}")
    private static String clientId;
    @Value("${spotifyApi.client-secret}")
    private static String clientSecret;

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

    public static void setAccessToken() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            
            log.info("Expires in: {}", clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

}
