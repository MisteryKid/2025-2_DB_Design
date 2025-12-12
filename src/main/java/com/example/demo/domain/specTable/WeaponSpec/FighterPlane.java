package com.example.demo.domain.specTable.WeaponSpec;

import com.example.demo.domain.specTable.airSpec;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fighter_spec")
@DiscriminatorValue("FIGHTER")
public class FighterPlane {
    /**
     * AirSpec의 PK를 FK로 사용하며 FighterSpec의 PK가 됩니다.
     */
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // AirSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private airSpec airSpec; // AirSpec 엔티티 참조

    // 전투기 고유 속성

    @Column(name = "wing_span", precision = 5, scale = 2)
    private BigDecimal wingSpan; // 날개 너비

    @Column(name = "max_g_force", precision = 3, scale = 1)
    private BigDecimal maxGForce; // G 한계
}
