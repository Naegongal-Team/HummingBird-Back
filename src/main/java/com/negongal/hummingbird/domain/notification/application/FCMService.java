package com.negongal.hummingbird.domain.notification.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.negongal.hummingbird.global.error.exception.InvalidException;

import lombok.extern.slf4j.Slf4j;

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

}
