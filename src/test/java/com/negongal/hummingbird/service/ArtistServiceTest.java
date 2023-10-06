package com.negongal.hummingbird.service;

import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
class ArtistServiceTest {

    @Autowired
    ArtistService artistService;

    @Test
    public void getArtistsTest() {

    }

}