package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SubmarineDetailDto {

    // --- Weapon (기본 정보) ---
    private String weapon_name;
    private Long weapon_id;

    // --- SeaSpec (해상 공통 스펙) 정보 ---
    private BigDecimal max_speed_knot;
    private BigDecimal displacement;
    private Integer sea_crew_capacity;
    private String armament;
    private String radar_type;

    // --- Submarine (잠수정 고유 스펙) 정보 ---
    private Integer max_diving_depth;
    private Integer torpedo_tubes_count;
    private String acoustic_signature;
}
