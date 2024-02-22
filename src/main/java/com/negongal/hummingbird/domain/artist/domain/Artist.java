package com.negongal.hummingbird.domain.artist.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "ARTIST", indexes = @Index(name = "idx_artist_name", columnList = "name", unique = true))
@Entity
public class Artist extends BaseTimeEntity implements Persistable<String> {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	private String image;

	@Basic(fetch = FetchType.LAZY)
	@Formula("(SELECT COUNT(1) FROM artist_heart ah WHERE ah.artist_id = id)")
	private Integer heartCount;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Genre> genres;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArtistHeart> hearts;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Track> topTracks;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<Performance> performances;

	@Builder
	public Artist(String id, String name, String image, List<Genre> genres, List<Track> topTracks) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.genres = genres;
		this.topTracks = topTracks;
		this.performances = new ArrayList<>();
		this.hearts = new ArrayList<>();
		this.heartCount = 0;
	}

	@Override
	public boolean isNew() {
		return this.getCreatedDate() == null;
	}
}
