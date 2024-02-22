package com.negongal.hummingbird.domain.chat.view;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negongal.hummingbird.domain.chat.dao.ChatMessageRepository;
import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewChatService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;

	/**
	 * 채팅 view 테스트용 서비스
	 */

	public List<ViewChatRoom> findAllRoom() {
		List<ChatRoom> chatRooms = chatRoomRepository.findAll();
		return chatRooms.stream().map(s -> ViewChatRoom.of(s)).collect(Collectors.toList());
	}

	public ViewChatRoom findByRoomId(String roomId) {
		ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
			.orElseThrow(() -> new NotExistException(CHAT_ROOM_NOT_EXIST));
		return ViewChatRoom.of(chatRoom);
	}

	public List<ViewChatMessage> loadMessage(String roomId) {
		ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
			.orElseThrow(() -> new NotExistException(CHAT_ROOM_NOT_EXIST));
		List<ChatMessage> chatMessageList = chatMessageRepository.findByIdOrderBySendTimeAsc(chatRoom.getId());
		return chatMessageList.stream().map(s -> ViewChatMessage.of(s))
			.collect(Collectors.toList());
	}
}
