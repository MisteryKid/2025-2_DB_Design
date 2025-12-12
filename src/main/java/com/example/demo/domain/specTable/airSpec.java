package com.example.demo.domain.specTable;


import com.example.demo.domain.Weapon;
import com.example.demo.domain.specTable.WeaponSpec.FighterPlane;
import com.example.demo.domain.specTable.WeaponSpec.Helicopter;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "air_spec")
// 하위 엔티티와의 상속을 위해 JOINED 전략 채택
@Inheritance(strategy = InheritanceType.JOINED)
public class airSpec {

    /**
     * Weapon의 PK를 FK로 사용하며 AirSpec의 PK가 됩니다.
     */
    @Id
    @Column(name = "weapon_id")
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Weapon의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private Weapon weapon; // Weapon 엔티티는 이전에 정의되었다고 가정

    // 공중 스펙 공통 속성

    @Column(name = "max_speed_mach", precision = 5, scale = 2)
    private BigDecimal maxSpeedMach; // 최고 속력 (단위: Mach)

    @Column(name = "reversal_speed", precision = 5, scale = 2)
    private BigDecimal reversalSpeed; // 반전 속력

    @Column(name = "climb_rate", precision = 10, scale = 2)
    private BigDecimal climbRate; // 상승률

    @Column(name = "crew_capacity")
    private Integer crewCapacity; // 승무원


    // AirSpec.java 클래스 내부

    // 1-1. 전투기 스펙 테이블 연결
    @OneToOne(mappedBy = "airSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FighterPlane fighterSpec;

    // 1-2. 헬리콥터 스펙 테이블 연결 (클래스 이름이 'Helicopter'라고 가정)
// 참고: 이전 SQL에서 테이블 이름을 'helicopter'로 사용했으므로, 클래스 이름도 'Helicopter'로 가정합니다.
    @OneToOne(mappedBy = "airSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Helicopter helicopter;

}
