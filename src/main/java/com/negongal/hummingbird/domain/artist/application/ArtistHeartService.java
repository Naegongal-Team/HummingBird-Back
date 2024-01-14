package com.negongal.hummingbird.domain.artist.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.artist.dao.ArtistHeartRepository;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistHeartService {

    private final ArtistHeartRepository artistHeartRepository;

    private final ArtistRepository artistRepository;

    private final UserRepository userRepository;

    @Transactional
    public void save(String artistId) throws FirebaseMessagingException {
        Long currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        ArtistHeart artistHeart = ArtistHeart.builder()
                .artist(artist)
                .user(user)
                .isAlarmed(false)
                .build();
        List<String> userToken = Arrays.asList(user.getFcmToken());
        TopicManagementResponse response = FirebaseMessaging.getInstance()
                .subscribeToTopic(userToken, artist.getName());
        System.out.println(response.getSuccessCount() + "tokens were subscribed successfully");
        artistHeartRepository.save(artistHeart);
    }

    @Transactional
    public void delete(String artistId) throws FirebaseMessagingException {
        Long currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
        ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(currentUserId, artistId)
                .orElseThrow(() -> new NoSuchElementException());
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        List<String> userToken = Arrays.asList(user.getFcmToken());
        TopicManagementResponse response = FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(userToken, artist.getName());
        System.out.println(response.getSuccessCount() + "tokens were subscribed successfully");
        artistHeartRepository.delete(artistHeart);
    }

    @Transactional
    public boolean modifyAlarm(String artistId) {
        Long currentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
        ArtistHeart artistHeart = artistHeartRepository.findByUserIdAndArtistId(currentUserId, artistId)
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_LIKE));
        boolean isAlarmed = artistHeart.getIsAlarmed();
        artistHeart.updateAlarmed();
        return isAlarmed;
    }

}
