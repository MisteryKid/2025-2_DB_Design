package com.example.demo.domain.specTable;


import com.example.demo.domain.Weapon;
import com.example.demo.domain.specTable.WeaponSpec.Artillery;
import com.example.demo.domain.specTable.WeaponSpec.Tank;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "land_spec")
@Inheritance(strategy = InheritanceType.JOINED)
public class landSpec {
    /**
     * Weapon의 PK를 FK로 사용하며 LandSpec의 PK가 됩니다.
     */
    @Id
    @Column(name = "weapon_id")
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Weapon의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private Weapon weapon; // Weapon 엔티티 참조 (이전에 정의되었다고 가정)

    // 지상 스펙 공통 속성

    @Column(name = "max_speed_kmh", precision = 5, scale = 2)
    private BigDecimal maxSpeedKmh; // 최고 속력 (단위: Km/h)

    @Column(name = "crew_capacity")
    private Integer crewCapacity; // 승무원

    @Column(name = "armor_thickness_mm")
    private Integer armorThicknessMm; // 장갑 두께 (mm)

    @Column(name = "tangent_capacity", precision = 10, scale = 2)
    private BigDecimal tangentCapacity; // 탄젠트 적재량 (DDL의 탄젠트 적재량 필드를 매핑)



    // LandSpec.java 클래스 내부

    // 2-1. 탱크 스펙 테이블 연결
    @OneToOne(mappedBy = "landSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tank tank;

    // 2-2. 견인포 스펙 테이블 연결
// 참고: 이전 CREATE TABLE에서 테이블 이름이 'tank_spec'과 동일하게 작성되었지만,
    @OneToOne(mappedBy = "landSpec", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Artillery artillery;



}
