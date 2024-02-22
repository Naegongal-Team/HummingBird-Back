package com.negongal.hummingbird.domain.chat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.chat.domain.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {

	@Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :id ORDER BY cm.sendTime ASC")
	List<ChatMessage> findByIdOrderBySendTimeAsc(@Param("id") Long id);
}
