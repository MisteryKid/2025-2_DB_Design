package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String website;
    private Integer foundedYear; // 설립 연도

    @Column(columnDefinition = "TEXT")
    private String description; // 추가 설명
}
