package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// 1. 상속 매핑 전략 지정: 단일 테이블 전략 사용
//@Inheritance(strategy = GenerationType.SINGLE_TABLE)
// 2. Discriminator 컬럼 지정: 어떤 하위 클래스인지 구분하는 컬럼
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Entity
@Table(name = "base_spec") // 테이블 이름 지정
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("BASE") // 이 자체로 인스턴스화되는 경우의 dtype 값 (보통은 사용 안 함)
public abstract class BaseSpec { // 3. 상속의 기본이므로 추상 클래스로 정의

    @Id // ⬅️ 기본 키로 지정
    @Column(name = "base_spec_id")
    private Long id; // ⬅️ 자동 증가(AUTO_INCREMENT) 어노테이션을 제거합니다.
//
//    // 2. 다른 엔티티에서 ID를 매핑 받음 (식별 관계의 핵심)
//    // 이 필드는 BaseSpec 테이블의 PK이자 FK 역할을 합니다.
//    @MapsId // ⬅️ Weapon 엔티티의 ID를 PK로 사용하도록 지정
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "base_spec_id") // ⬅️ FK 컬럼명을 PK 컬럼명과 동일하게 설정
//    private Weapon weapon; // ⬅️ BaseSpec이 누구의 스펙인지 역참조 필드 추
//
//    // 최대 속력 (DECIMAL(10, 2) 반영)
//    @Column(name = "max_speed", precision = 10)
//    private BigDecimal maxSpeed;
//
//    // 최대 고도 (INT 반영)
//    @Column(name = "max_altitude")
//    private Integer maxAltitude;
//
//    // 엔진 타입 (VARCHAR(50) 반영)
//    @Column(name = "engine_type", length = 50)
//    private String engineType;

}