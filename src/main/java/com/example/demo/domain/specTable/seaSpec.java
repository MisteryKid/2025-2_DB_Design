package com.example.demo.domain.specTable;


import com.example.demo.domain.Weapon;
import com.example.demo.domain.specTable.WeaponSpec.Submarine;
import com.example.demo.domain.specTable.WeaponSpec.Vessel;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sea_spec")
public class seaSpec {

    @Id
    @Column
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn
    private Weapon weapon;

    // 해상 스펙 공통 속성

    @Column(name = "max_speed_knot", precision = 5, scale = 2)
    private BigDecimal maxSpeedKnot; // 최고 속력 (단위: knot)

    @Column(name = "displacement", precision = 10, scale = 2)
    private BigDecimal displacement; // 배수량

    @Column(name = "crew_capacity")
    private Integer crewCapacity; // 승무원

    @Column(name = "armament", length = 255)
    private String armament; // 무장

    @Column(name = "radar_type", length = 50)
    private String radarType; // 레이더



//    @OneToOne(mappedBy = "seaSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Vessel vessel;
//
//    @OneToOne(mappedBy = "seaSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Submarine submarine;
}
