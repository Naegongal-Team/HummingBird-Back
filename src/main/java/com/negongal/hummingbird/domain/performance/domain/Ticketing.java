package com.negongal.hummingbird.domain.performance.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticketing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id")
	private Performance performance;

	@Enumerated(EnumType.STRING)
	private TicketType ticketType;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;

	private String platform;
	private String link;

	@Builder
	public Ticketing(Performance performance, TicketType ticketType, LocalDateTime startDate, String platform,
		String link) {
		this.performance = performance;
		this.ticketType = ticketType;
		this.startDate = startDate;
		this.platform = platform;
		this.link = link;

		performance.getTicketings().add(this);
	}
}
