package com.negongal.hummingbird.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistService {

    @Transactional(readOnly = true)
    public void getArtists() {

    }
}
