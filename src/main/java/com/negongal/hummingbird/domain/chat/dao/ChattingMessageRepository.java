package com.negongal.hummingbird.domain.chat.dao;

import com.negongal.hummingbird.domain.chat.domain.ChattingMessage;
import com.negongal.hummingbird.domain.chat.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

}
