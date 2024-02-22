package com.negongal.hummingbird.domain.chat.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;

public interface ChatMessageRepositoryCustom {

	Page<ChatMessageResponseDto> findAllCustom(Long roomId, Pageable pageable);
}
