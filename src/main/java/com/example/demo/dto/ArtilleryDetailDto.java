package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ArtilleryDetailDto {
    // --- Weapon (기본 정보) ---
    private String weapon_name;
    private Long weapon_id;

    // --- LandSpec (지상 공통 스펙) 정보 ---
    // SQL 쿼리의 AS 별칭과 일치하도록 정의
    private BigDecimal max_speed_kmh;
    private Integer land_crew_capacity; // AS 별칭이 'land_crew_capacity'라고 가정
    private Integer armor_thickness_mm;
    private BigDecimal tangent_capacity;

    // --- ArtillerySpec (자주포 고유 스펙) 정보 ---
    // 이전 테이블 정의를 참고하여 고유 필드를 가정합니다.
    private Integer gun_caliber;          // 주포 구경 (mm)
    private Integer armor_thickness;         // 최대 사거리 (km) (가정)
}
