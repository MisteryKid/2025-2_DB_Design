package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModelLink {

    @Id // 이 필드를 테이블의 기본 키(Primary Key)로 지정합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 DB에 위임합니다.
    private Long id; // 프라모델 식별자 (PK)

    @Column(nullable = false)
    private String weapon;

    @Column(nullable = false, length = 100)
    private String name; // 프라모델 이름 (예: "PG UNICORN GUNDAM")

    private String series; // 시리즈 (예: "GUNDAM", "MACROSS")

    @Column(columnDefinition = "TEXT")
    private String description; // 상세 설명

    private String imageUrl; // 프라모델 대표 이미지 URL (예: "/images/pg_unicorn.jpg")

    // ----------- 링크 정보 필드 -----------
    @Column(length = 500) // URL은 길 수 있으므로 충분한 길이를 할당합니다.
    private String productPageUrl; // 제조사 상품 페이지 또는 판매처 링크

    @Column(length = 500)
    private String reviewPageUrl; // 리뷰 링크 (블로그, 유튜브 등)


}
