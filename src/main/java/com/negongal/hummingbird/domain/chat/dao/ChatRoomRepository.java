package com.negongal.hummingbird.domain.chat.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.negongal.hummingbird.domain.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	Optional<ChatRoom> findByRoomId(String roomId);
}
