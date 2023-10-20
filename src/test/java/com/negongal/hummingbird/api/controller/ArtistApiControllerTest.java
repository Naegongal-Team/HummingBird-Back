package com.negongal.hummingbird.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negongal.hummingbird.api.dto.ArtistDto;
import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.domain.ArtistLike;
import com.negongal.hummingbird.repository.ArtistLikeRepository;
import com.negongal.hummingbird.repository.ArtistRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
class ArtistApiControllerTest {

    @Autowired
    private ArtistApiController artistApiController;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistLikeRepository artistLikeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @After
    public void clearRepository() {
        artistLikeRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void getArtistListsTest() throws Exception {
        mockMvc.perform(get("/artist")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getArtistSearchByNameTest() throws Exception {
        mockMvc.perform(get("/artist/search/{artistId}", "ABBA")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getFailedArtistSearchByNameTest() throws Exception {
        mockMvc.perform(get("/artist/search/{artistId}", "Mario")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}