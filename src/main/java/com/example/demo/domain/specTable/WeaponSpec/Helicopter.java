package com.example.demo.domain.specTable.WeaponSpec;

import com.example.demo.domain.specTable.airSpec;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
// DDL에서는 테이블명이 'helicopter'였으나, 엔티티의 일관성을 위해
// 클래스 이름은 Helicopter로 하고 테이블명은 'helicopter'로 유지합니다.
@Table(name = "helicopter")
@DiscriminatorValue("HELICOPTER")
public class Helicopter {

    /**
     * AirSpec의 PK를 FK로 사용하며 Helicopter의 PK가 됩니다.
     */
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // AirSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private airSpec airSpec; // AirSpec 엔티티 참조

    // 헬기 고유 속성

    @Column(name = "rotor_speed_rpm")
    private Integer rotorSpeedRpm; // 로터 회전 속력 (RPM)

    @Column(name = "vertical_climb_rate", precision = 5, scale = 2)
    private BigDecimal verticalClimbRate; // 수직 상승률

}