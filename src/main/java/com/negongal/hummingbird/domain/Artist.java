package com.negongal.hummingbird.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter @ToString
public class Artist {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Lob
        private String description;

        private String images;

        private String genres;
}
