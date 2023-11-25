package com.negongal.hummingbird.domain.chat.dao;

import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :id ORDER BY cm.sendTime DESC")
    List<ChatMessage> findByIdOrderBySendTimeAsc(@Param("id") Long id);
}
