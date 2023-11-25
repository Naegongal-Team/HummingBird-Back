package com.negongal.hummingbird.domain.chat.domain;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
//    private final List<ChattingMessage> chattingMessageList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Builder
    public ChattingRoom(Performance performance) {
        this.performance = performance;
    }

//    public void createChatting(ChattingMessage chatting) {
//        chattingMessageList.add(chatting);
////        User sender = chatting.getUser();
////
////        MemberChattingRoom memberChattingRoom = MemberChattingRoom.builder()
////                .member(sender)
////                .chattingRoom(this)
////                .build();
////        memberChattingRooms.add(memberChattingRoom);
////        sender.createChatting(memberChattingRoom);
//    }
}
