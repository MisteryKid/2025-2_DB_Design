package com.example.demo.domain.specTable.WeaponSpec;

import com.example.demo.domain.specTable.landSpec;
import jakarta.persistence.*;

@Entity
@Table(name = "artillery_spec") // 테이블 이름을 artillery_spec으로 가정
@DiscriminatorValue("ARTILLERY")
public class Artillery {
    /**
     * LandSpec의 PK를 FK로 사용하며 ArtillerySpec의 PK가 됩니다.
     */
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // LandSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private landSpec landSpec; // LandSpec 엔티티 참조

    // 견인포 고유 속성 (DDL의 견인포 필드를 매핑)

    @Column(name = "armor_thickness")
    private Integer armorThickness; // 장갑 두께 (mm)

    @Column(name = "gun_caliber")
    private Integer gunCaliber; // 주포 구경 (mm)

}
