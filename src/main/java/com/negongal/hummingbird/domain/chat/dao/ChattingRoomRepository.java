package com.negongal.hummingbird.domain.chat.dao;

import com.negongal.hummingbird.domain.chat.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
