package com.negongal.hummingbird.domain.chat.service;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.dao.RedisRepository;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.chat.dto.ChatRoomDto;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.global.common.pubsub.RedisSubscriber;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final PerformanceRepository performanceRepository;
	private final ChatRoomRepository chatRoomRepository;

	private final RedisRepository redisRepository;
	private final RedisSubscriber redisSubscriber;
	private final RedisMessageListenerContainer redisMessageListener;
	private Map<String, ChannelTopic> topics;

	@PostConstruct
	private void init() {
		topics = new HashMap<>();
	}

	/**
	 * 채팅방 생성
	 */
	public String createChatRoom(Long performanceId) {
		Performance performance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

		// redis
		ChatRoomDto chatRoomDto = ChatRoomDto.create();
		redisRepository.saveRoom(chatRoomDto);

		// db
		ChatRoom chatRoom = ChatRoom.builder()
			.roomId(chatRoomDto.getRoomId())
			.performance(performance)
			.build();
		chatRoomRepository.save(chatRoom);

		return chatRoom.getRoomId();
	}

	/**
	 * 채팅방 입장
	 */
	public void enterChatRoom(String roomId) {
		log.info("roomId {}", roomId);
		ChannelTopic topic = topics.get(roomId);
		if (topic == null) {
			topic = new ChannelTopic(roomId);
		}
		redisMessageListener.addMessageListener(redisSubscriber, topic);
		topics.put(roomId, topic);
	}

	public ChannelTopic getTopic(String roomId) {
		return topics.get(roomId);
	}
}
