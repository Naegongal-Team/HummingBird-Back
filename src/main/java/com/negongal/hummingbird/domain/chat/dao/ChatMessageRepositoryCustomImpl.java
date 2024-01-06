package com.negongal.hummingbird.domain.chat.dao;

import static com.negongal.hummingbird.domain.chat.domain.QChatMessage.chatMessage;

import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.chat.dto.QChatMessageResponseDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatMessageResponseDto> findAllCustom(Long roomId, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        JPQLQuery<ChatMessageResponseDto> dtoQuery = queryFactory
                .select(new QChatMessageResponseDto(chatMessage.user.nickname, chatMessage.type,
                        chatMessage.content, chatMessage.sendTime, chatMessage.user.profileImage))
                .from(chatMessage)
                .where(chatMessage.chatRoom.id.eq(roomId))
                .orderBy(chatMessage.sendTime.desc());

        Long count = queryFactory
                .select(new QChatMessageResponseDto(chatMessage.user.nickname, chatMessage.type,
                        chatMessage.content, chatMessage.sendTime, chatMessage.user.profileImage))
                .where(chatMessage.chatRoom.id.eq(roomId))
                .from(chatMessage).stream().count();

        dtoQuery.offset(offset);
        dtoQuery.limit(pageSize);

        List<ChatMessageResponseDto> messages = dtoQuery.offset(offset).limit(pageSize).fetch();
        // 페이지별로 sendTime을 오름차순으로 정렬
        messages.sort(Comparator.comparing(ChatMessageResponseDto::getSendTime));

        return new PageImpl<>(messages, pageable, count);
    }
}
