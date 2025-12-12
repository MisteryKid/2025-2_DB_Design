package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class FighterDetailDto {
    // --- Weapon (기본 정보) ---
    private String weapon_name;
    private Long weapon_id;

    // --- AirSpec (공중 공통 스펙) 정보 ---
    private BigDecimal max_speed_mach;
    private BigDecimal reversal_speed;
    private BigDecimal climb_rate;
    private Integer air_crew_capacity; // AS 별칭이 'air_crew_capacity'라고 가정

    // --- FighterSpec (전투기 고유 스펙) 정보 ---
    private BigDecimal wing_span;      // 날개 너비
    private BigDecimal max_g_force;    // G 한계
}
