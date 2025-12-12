package com.example.demo.domain.specTable.WeaponSpec;

import com.example.demo.domain.specTable.landSpec;
import jakarta.persistence.*;

@Entity
@Table(name = "tank_spec")
@DiscriminatorValue("TANK")
public class Tank {
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // LandSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private landSpec landSpec; // LandSpec 엔티티 참조

    // 탱크 고유 속성

    // DDL에 armor_thickness가 LandSpec과 TankSpec에 중복되어 있었으므로,
    // 여기서는 탱크의 고유한 장갑 두께 필드를 명확히 매핑합니다.
    @Column(name = "armor_thickness")
    private Integer armorThickness; // 장갑 두께 (mm)

    @Column(name = "gun_caliber")
    private Integer gunCaliber; // 주포 구경 (mm)
}
