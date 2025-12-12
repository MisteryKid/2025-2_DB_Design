package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class HelicopterDetailDto {
    // --- Weapon (기본 정보) ---
    private String weapon_name;
    private Long weapon_id;

    // --- AirSpec (공중 공통 스펙) 정보 ---
    // SQL 쿼리의 AS 별칭과 일치하도록 정의
    private BigDecimal max_speed_mach;
    private BigDecimal reversal_speed;
    private BigDecimal climb_rate;
    private Integer air_crew_capacity; // AS 별칭이 'air_crew_capacity'라고 가정

    // --- Helicopter (헬기 고유 스펙) 정보 ---
    private Integer rotor_speed_rpm;           // 로터 회전 속력 (RPM)
    private BigDecimal vertical_climb_rate;    // 수직 상승률
}
