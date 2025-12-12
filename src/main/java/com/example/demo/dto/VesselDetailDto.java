package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class VesselDetailDto {
    private String weapon_name;
    private Long weapon_id;
    private BigDecimal max_speed_knot;
    private BigDecimal displacement;
    private Integer sea_crew_capacity;
    private String armament;
    private String radar_type;
    private BigDecimal accuracy_speed;
    private BigDecimal max_speed_vessel;
    private BigDecimal buoyancy_vessel;
}
