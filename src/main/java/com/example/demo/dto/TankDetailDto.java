package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class TankDetailDto {

    // --- Weapon (기본 정보) ---
    private String weapon_name;
    private Long weapon_id;

    // --- LandSpec (지상 공통 스펙) 정보 ---
    // SQL 쿼리의 AS 별칭과 일치하도록 정의
    private BigDecimal max_speed_kmh;
    private Integer land_crew_capacity;
    private Integer armor_thickness_mm;
    private BigDecimal tangent_capacity;

    // --- TankSpec (전차 고유 스펙) 정보 ---
    // 이전 테이블 정의를 참고하여 고유 필드를 가정합니다.
    private Integer gun_caliber;        // 주포 구경 (mm)
    private Integer armor_thickness;    // 장갑 두께 (mm, tank_spec 고유 필드)
    private Integer operational_range_km; // 작전 반경 (km) (가정)

}
