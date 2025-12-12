package com.example.demo.repository;

import com.example.demo.domain.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {
    List<Weapon> findByNameContaining(String name);

    // 여러 개의 Category ID 목록(Collection)을 사용하여 무기를 검색합니다.
    List<Weapon> findByCategory_IdIn(Collection<Long> categoryIds);

    // 1. 이름에 특정 키워드가 포함된 무기를 대소문자 구분 없이 조회합니다.
    List<Weapon> findByNameContainingIgnoreCase(String name);

    List<Weapon> findByCategory_Id(Long categoryId);

    Optional<Weapon> findDetailById(@Param("weaponId") Long weaponId);

    // 💡 네이티브 쿼리 사용!
    // 결과를 매핑할 엔티티가 없으므로, Map 또는 결과 컬럼에 맞는 DTO를 반환해야 합니다.
    //// 함정
    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    SS.max_speed_knot, " +
                    "    SS.displacement, " +
                    "    SS.crew_capacity AS sea_crew_capacity, " +
                    "    SS.armament, " +
                    "    SS.radar_type, " +
                    "    V.accuracy_speed, " +
                    "    V.max_speed_vessel, " +
                    "    V.buoyancy_vessel " +
                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    sea_spec SS ON W.weapon_id = SS.weapon_weapon_id " +
                    "JOIN " +
                    "    vessel V ON SS.weapon_weapon_id = V.weapon_id " +
                    //"LEFT JOIN " +
                    //"    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findVesselDetailNative(@Param("weaponId") Long weaponId);


    //// 잠수함
    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    SS.max_speed_knot, " +
                    "    SS.displacement, " +
                    "    SS.crew_capacity AS sea_crew_capacity, " +
                    "    SS.armament, " +
                    "    SS.radar_type, " +
                    "    S.max_diving_depth, " +        // Submarine 필드
                    "    S.torpedo_tubes_count, " +     // Submarine 필드
                    "    S.acoustic_signature " +       // Submarine 필드
                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    sea_spec SS ON W.weapon_id = SS.weapon_weapon_id " +
                    "JOIN " +
                    "    submarine S ON SS.weapon_weapon_id = S.weapon_id " + // Submarine 조인
//                    "LEFT JOIN " +
//                    "    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findSubmarineDetailNative(@Param("weaponId") Long weaponId);


    //// 헬기
    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    ASpec.max_speed_mach, " +
                    "    ASpec.reversal_speed, " +
                    "    ASpec.climb_rate, " +
                    "    ASpec.crew_capacity AS air_crew_capacity, " + // 컬럼명 충돌을 피하기 위한 별칭
                    "    H.rotor_speed_rpm, " +
                    "    H.vertical_climb_rate " +
                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    air_spec ASpec ON W.weapon_id = ASpec.weapon_id " +
                    "JOIN " +
                    "    helicopter H ON ASpec.weapon_id = H.weapon_id " + // 💡 수정된 조인 조건 사용
//                    "LEFT JOIN " +
//                    "    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findHelicopterDetailNative(@Param("weaponId") Long weaponId);


    // 전투기 sql
    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    ASpec.max_speed_mach, " +
                    "    ASpec.reversal_speed, " +
                    "    ASpec.climb_rate, " +
                    "    ASpec.crew_capacity AS air_crew_capacity, " +
                    "    F.wing_span, " +              // FighterSpec 고유 스펙
                    "    F.max_g_force " +             // FighterSpec 고유 스펙
                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    air_spec ASpec ON W.weapon_id = ASpec.weapon_id " +
                    "JOIN " +
                    "    fighter_spec F ON ASpec.weapon_id = F.weapon_id " + // fighter_spec 테이블 조인
                    // 💡 ModelLink 테이블 LEFT JOIN
//                    "LEFT JOIN " +
//                    "    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findFighterDetailNative(@Param("weaponId") Long weaponId);


    // 자주포
// WeaponRepository.java 내부에 추가

    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    LSpec.max_speed_kmh, " +
                    "    LSpec.crew_capacity AS land_crew_capacity, " +
                    "    LSpec.armor_thickness_mm, " +
                    "    LSpec.tangent_capacity, " +
                    "    A.gun_caliber, " +
                    "    A.armor_thickness " +
//                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    land_spec LSpec ON W.weapon_id = LSpec.weapon_id " +
                    "JOIN " +
                    "    artillery_spec A ON LSpec.weapon_id = A.weapon_id " + // 조인 조건: weapon_id 사용
//                    "LEFT JOIN " +
//                    "    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findArtilleryDetailNative(@Param("weaponId") Long weaponId);


    // 탱크
    @Query(value =
            "SELECT " +
                    "    W.name AS weapon_name, " +
                    "    W.weapon_id, " +
                    "    LSpec.max_speed_kmh, " +
                    "    LSpec.crew_capacity AS land_crew_capacity, " +
                    "    LSpec.armor_thickness_mm, " +
                    "    LSpec.tangent_capacity, " +
                    "    T.gun_caliber, " +
                    "    T.armor_thickness " +
                    // 💡 ModelLink 필드 추가
//                    "    ML.product_page_url, " + // 프라모델 판매 링크
//                    "    ML.series, " +          // 무기 시리즈 명
//                    "    ML.image_url " +        // 이미지 URL
                    "FROM " +
                    "    Weapon W " +
                    "JOIN " +
                    "    land_spec LSpec ON W.weapon_id = LSpec.weapon_id " + // land_spec 테이블 조인
                    "JOIN " +
                    "    tank_spec T ON LSpec.weapon_id = T.weapon_id " + // tank_spec 테이블 조인 (T.weapon_id 사용)
                    //"LEFT JOIN " +
                    //"    model_link ML ON W.weapon_id = ML.weapon " + // Weapon ID와 model_link의 weapon FK 연결
                    "WHERE " +
                    "    W.weapon_id = :weaponId",
            nativeQuery = true)
    Optional<Map<String, Object>> findTankDetailNative(@Param("weaponId") Long weaponId);


    // WeaponRepository.java 내부에 추가

// WeaponRepository.java 내부에 추가 (수정됨)

    @Query(value =
            "WITH RECURSIVE successor_chain AS (" +
                    "    SELECT W.weapon_id, W.name, W.previous_model_id, 1 AS generation_level " +
                    "    FROM weapon W " +
                    "    WHERE W.previous_model_id = :startWeaponId " + // 💡 startWeaponId 파라미터 사용
                    "    UNION ALL " +
                    "    SELECT W.weapon_id, W.name, W.previous_model_id, SC.generation_level + 1 AS generation_level " +
                    "    FROM weapon W " +
                    "    INNER JOIN successor_chain SC ON W.previous_model_id = SC.weapon_id " +
                    ")" +
                    "SELECT " +
                    "    T.weapon_id, " +
                    "    T.name AS successor_name, " +
                    "    T.generation_level, " +
                    "    (SELECT name FROM weapon WHERE weapon_id = T.previous_model_id) AS previous_model_name " +
                    "FROM " +
                    "    successor_chain T " +
                    "ORDER BY " +
                    "    T.generation_level", // 💡 정확한 별칭 generation_level 사용
            nativeQuery = true)
    List<Map<String, Object>> findSuccessorsNative(@Param("startWeaponId") Long startWeaponId);


    @Query(value =
            "WITH RECURSIVE predecessor_chain AS (" +
                    "    SELECT W.weapon_id, W.name, W.previous_model_id, 0 AS generation_level " +
                    "    FROM weapon W " +
                    "    WHERE W.weapon_id = :startWeaponId " + // 💡 입력된 ID를 시작점으로 설정
                    "    UNION ALL " +
                    "    SELECT P.weapon_id, P.name, P.previous_model_id, PC.generation_level + 1 AS generation_level " +
                    "    FROM weapon P " +
                    "    INNER JOIN predecessor_chain PC ON P.weapon_id = PC.previous_model_id " + // 💡 역방향 추적
                    ")" +
                    "SELECT " +
                    "    T.weapon_id, " +
                    "    T.name AS predecessor_name, " + // successor_name 대신 predecessor_name 사용
                    "    T.generation_level, " +
                    "    (SELECT name FROM weapon WHERE weapon_id = T.previous_model_id) AS previous_model_name " +
                    "FROM " +
                    "    predecessor_chain T " +
                    "WHERE " +
                    "    T.generation_level > 0 " + // 💡 시작 모델(0세대) 제외
                    "ORDER BY " +
                    "    T.generation_level DESC", // 가장 오래된 모델부터 정렬
            nativeQuery = true)
    List<Map<String, Object>> findPredecessorsNative(@Param("startWeaponId") Long startWeaponId);


    // 하나의 무기에 대한 프라모델 리스트
    @Query(value =
            "SELECT " +
                    "    ML.id, " + // model_link의 고유 ID (필요한 경우)
                    "    ML.product_page_url, " + // 프라모델 판매 링크
                    "    ML.series, " +          // 무기 시리즈 명
                    "    ML.image_url, " +        // 이미지 URL
                    "    ML.description, " +      // 상세 설명
                    "    ML.name "+
                    "FROM " +
                    "    model_link ML " +
                    "WHERE " +
                    "    ML.weapon = :weaponId", // Weapon ID로 필터링
            nativeQuery = true)
    List<Map<String, Object>> findModelLinksByWeaponId(@Param("weaponId") Long weaponId);


}
