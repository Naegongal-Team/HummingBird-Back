package com.negongal.hummingbird.service;

import com.negongal.hummingbird.config.SpotifyConfig;
import com.negongal.hummingbird.repository.ArtistRepository;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
class ArtistServiceTest {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistService artistService;
}