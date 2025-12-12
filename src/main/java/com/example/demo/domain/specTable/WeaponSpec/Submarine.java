package com.example.demo.domain.specTable.WeaponSpec;


import com.example.demo.domain.specTable.seaSpec;
import jakarta.persistence.*;

@Entity
@Table(name = "submarine")
@DiscriminatorValue("SUBMARINE")
public class Submarine {
    @Id
    @Column(name = "weapon_id") // base_spec_id 대신 weapon_id 사용
    private Long weaponId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // SeaSpec의 ID를 이 엔티티의 ID로 매핑
    @JoinColumn(name = "weapon_id")
    private seaSpec seaSpec;

    // 잠수정 고유 속성

    @Column(name = "max_diving_depth")
    private Integer maxDivingDepth; // 최대 잠항 깊이 (단위: 미터)

    @Column(name = "torpedo_tubes_count")
    private Integer torpedoTubesCount; // 어뢰 발사관 수

    @Column(name = "acoustic_signature", length = 50)
    private String acousticSignature; // 음향 시그니처
}
