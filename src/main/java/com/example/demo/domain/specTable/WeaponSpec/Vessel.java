package com.example.demo.domain.specTable.WeaponSpec;

import com.example.demo.domain.specTable.seaSpec;
import jakarta.persistence.*;

import java.math.BigDecimal;


/*
$SeaSpec$을 부모로 하는 $Vessel$ (함정) 엔티티를 구현
\SeaSpec$과도 $PK/FK$를 통한 1:1 관계를 가집니다.
 */

@Entity
@Table(name = "vessel")
@DiscriminatorValue("VESSEL")
public class Vessel {
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // SeaSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private seaSpec seaSpec;

    // 함정 고유 속성

    @Column(name = "accuracy_speed", precision = 5, scale = 2)
    private BigDecimal accuracySpeed;  // 정확 속도

    @Column(name = "max_speed_vessel", precision = 5, scale = 2)
    private BigDecimal maxSpeedVessel; // 최대 속력 (vessel 고유 필드)

    @Column(name = "buoyancy_vessel", precision = 10, scale = 2)
    private BigDecimal buoyancyVessel;  // 부양력


}
