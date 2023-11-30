package com.negongal.hummingbird.global.common.pubsub;

import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageDto message) {
//        log.info("RedisPublisher :: {} - {}", topic.getTopic(), message.toString());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}