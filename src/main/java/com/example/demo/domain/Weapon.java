package com.example.demo.domain;


import com.example.demo.domain.specTable.airSpec;
import com.example.demo.domain.specTable.landSpec;
import com.example.demo.domain.specTable.seaSpec;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 1. 이 클래스가 JPA 엔티티임을 명시하며, DB 테이블과 매핑됩니다.
@Table(name = "weapon") // 2. 매핑될 테이블의 이름을 'weapon'으로 지정합니다.
@Getter // 3. Lombok: 모든 필드의 Getter 메서드를 자동 생성합니다.
@Setter // 4. Lombok: 모든 필드의 Setter 메서드를 자동 생성합니다.
@NoArgsConstructor // 5. Lombok: 기본 생성자를 자동 생성합니다. (JPA 필수)
public class Weapon {

    @Id // 6. 이 필드를 테이블의 기본 키(Primary Key)로 지정합니다.
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // 7. 기본 키 생성 전략을 DB에 위임합니다. (MySQL/MariaDB의 AUTO_INCREMENT)
    @Column(name = "weapon_id")
    private Long id; // 무기 식별자 (PK)

    // 2. 이름 (NOT NULL, UNIQUE)
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    // 3. 이전 모델 (previous_model_id) - 재귀적 ManyToOne
    // SQL: FOREIGN KEY (previous_model_id) REFERENCES weapon(weapon_id)
//    @OneToOne(fetch = FetchType.LAZY) // 지연 로딩 설정 (필요할 때만 로드)
//    @JoinColumn(name = "previous_model_id", nullable = false) // ⬅️ nullable=true 명시
//    private Weapon previousModel;

    // 4. 제조사 (manufacturer_id) - ManyToOne (NOT NULL)
    // SQL: FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(manufacturer_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false) // NOT NULL 조건 반영
    private Manufacturer manufacturer; // Manufacturer 엔티티가 필요함

//    @Column(columnDefinition = "TEXT") // 10. 긴 문자열을 저장할 때 사용 (DB의 TEXT 타입)
//    private String description; // 무기 설명

    // 5. 카테고리 (category_id) - ManyToOne (Nullable)
    // SQL: FOREIGN KEY (category_id) REFERENCES category(category_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // Category 엔티티가 필요함

    // 6. 플랫폼 (platform_id) - ManyToOne (NOT NULL)
    // SQL: FOREIGN KEY (platform_id) REFERENCES usage_location(location_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", nullable = false) // NOT NULL 조건 반영
    private Platform platform; // UsageLocation 엔티티가 필요함 (테이블 이름 가정)

    @Column
    private String country;

    // 1. 해상 스펙 (SeaSpec)과의 1:1 관계
    // SeaSpec 엔티티에서 weapon 필드에 의해 매핑되었음을 명시
    @OneToOne(mappedBy = "weapon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private seaSpec seaSpec;

    // 2. 공중 스펙 (AirSpec)과의 1:1 관계
    @OneToOne(mappedBy = "weapon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private airSpec airSpec;

    // 3. 지상 스펙 (LandSpec)과의 1:1 관계
    @OneToOne(mappedBy = "weapon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private landSpec landSpec;

    // 💡 7. 선행 모델 (previous_model_id) - 재귀적 ManyToOne (Nullable)
    // SQL: FOREIGN KEY (previous_model_id) REFERENCES weapon(weapon_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_model_id") // nullable을 명시적으로 지정하지 않으면 기본값은 true (NULL 허용)
    private Weapon previousModel;


    public Long getPlatformId() {
        // Platform 엔티티에 getId() 또는 platformId 필드에 접근하는 메서드가 있다고 가정
        if (this.platform != null) {
            return this.platform.getId(); // Platform 엔티티의 PK 필드명(getId)으로 가정
        }
        return null;
    }

    /**
     * Category 엔티티를 통해 category_id 값을 가져오는 편의 메서드
     */
    public Long getCategoryId() {
        if (this.category != null) {
            // Category 엔티티의 기본 키 필드명(getId)으로 가정합니다.
            return this.category.getId();
        }
        return null;
    }
}
