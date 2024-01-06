package com.negongal.hummingbird.domain.chat.dao;

import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :id ORDER BY cm.sendTime ASC")
    List<ChatMessage> findByIdOrderBySendTimeAsc(@Param("id") Long id);
}
