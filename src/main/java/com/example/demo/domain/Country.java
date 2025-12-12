package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Country {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @Column(nullable = false, unique = true) // name 컬럼에 UNIQUE 제약 조건 추가
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}