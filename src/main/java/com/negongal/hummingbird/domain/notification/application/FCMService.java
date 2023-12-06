package com.negongal.hummingbird.domain.notification.application;

<<<<<<< HEAD
<<<<<<< HEAD

import static com.negongal.hummingbird.global.error.ErrorCode.PUSH_MESSAGE_FAILED;

=======
>>>>>>> a91a356 (feat: fcm 토큰 저장을 위해 User class 수정)
=======

import static com.negongal.hummingbird.global.error.ErrorCode.PUSH_MESSAGE_FAILED;

>>>>>>> 15d91e7 (feat: FCM서비스 클래스 작성)
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
<<<<<<< HEAD
<<<<<<< HEAD
import com.negongal.hummingbird.global.error.exception.InvalidException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class FCMService {
    public void sendMessageByTopic(String topicName, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setTopic(topicName)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException ex) {
            throw new InvalidException(PUSH_MESSAGE_FAILED);
        }
    }

    public void sendMessageByToken(String fcmToken, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(fcmToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException ex) {
            throw new InvalidException(PUSH_MESSAGE_FAILED);
        }
    }

    public void sendMessageBySeveralTokens(List<String> tokens, String title, String body) {
        /*
        고민 좀 해봐야 할 거 가튼데
         */
    }
=======
import com.negongal.hummingbird.domain.performance.domain.Performance;
=======
import com.negongal.hummingbird.global.error.exception.InvalidException;
>>>>>>> 15d91e7 (feat: FCM서비스 클래스 작성)
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class FCMService {
    public void sendMessageByTopic(String topicName, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setTopic(topicName)
                .build();

<<<<<<< HEAD
>>>>>>> a91a356 (feat: fcm 토큰 저장을 위해 User class 수정)
=======
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException ex) {
            throw new InvalidException(PUSH_MESSAGE_FAILED);
        }
    }

    public void sendMessageByToken(String fcmToken, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setToken(fcmToken)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException ex) {
            throw new InvalidException(PUSH_MESSAGE_FAILED);
        }
    }
>>>>>>> 15d91e7 (feat: FCM서비스 클래스 작성)
}
