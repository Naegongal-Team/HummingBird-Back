package com.negongal.hummingbird.domain.performance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.performance.domain.Ticketing;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long> {
}
