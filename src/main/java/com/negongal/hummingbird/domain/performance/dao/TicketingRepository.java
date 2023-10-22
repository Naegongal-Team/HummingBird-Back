package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long> {
}
