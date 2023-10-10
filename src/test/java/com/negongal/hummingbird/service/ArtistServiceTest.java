package com.negongal.hummingbird.service;

import com.negongal.hummingbird.repository.ArtistRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ArtistServiceTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistService artistService;


}