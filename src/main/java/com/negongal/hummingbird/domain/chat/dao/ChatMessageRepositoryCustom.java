package com.negongal.hummingbird.domain.chat.dao;


import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageRepositoryCustom {

    Page<ChatMessageResponseDto> findAllCustom(Long roomId, Pageable pageable);
}
