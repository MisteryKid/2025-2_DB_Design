package com.example.demo.service;


import com.example.demo.domain.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WeaponService {

    private final WeaponRepository weaponRepository;
    private final ManufactureRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final PlatformRepository platformRepository;
    private final BaseSpecRepository baseSpecRepository;

    // 생성자 (필드 주입)
    public WeaponService(WeaponRepository weaponRepository,
                         ManufactureRepository manufacturerRepository,
                         CategoryRepository categoryRepository,
                         PlatformRepository platformRepository,
                         BaseSpecRepository baseSpecRepository) {
        this.weaponRepository = weaponRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.platformRepository = platformRepository;
        this.baseSpecRepository = baseSpecRepository;
    }

    // 1. 무기의 플랫폼 ID를 조회하는 메서드 추가
    public Long getPlatformId(Long weaponId) {
        return weaponRepository.findById(weaponId) // 기본 findById 사용
                .map(Weapon::getPlatformId) // Weapon 엔티티의 platformId 필드를 가져옴
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 무기를 찾을 수 없습니다."
                ));
    }
    /**
     * 특정 무기 ID의 category_id를 조회합니다.
     */
    public Long getCategoryId(Long weaponId) {
        return weaponRepository.findById(weaponId) // findById를 사용하여 Weapon 엔티티를 조회
                .map(Weapon::getCategoryId)       // 💡 위에서 추가한 getCategoryId() 메서드 사용
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 무기를 찾을 수 없습니다."
                ));
    }

    // Vessel 상세 조회 메서드
    public Map<String, Object> getVesselDetailNative(Long weaponId) {
        return weaponRepository.findVesselDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 무기를 찾을 수 없습니다."
                ));
    }

    // Submarine 상세 조회 메서드
    public Map<String, Object> getSubmarineDetailNative(Long weaponId) {
        // 💡 Repository에서 정의한 findSubmarineDetailNative 메서드를 호출
        return weaponRepository.findSubmarineDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 잠수정 상세 스펙을 찾을 수 없습니다."
                ));
    }

    // 헬기 상세 조회 메서드
    public Map<String, Object> getHelicopterDetailNative(Long weaponId) {
        // 💡 Repository에서 정의한 findHelicopterDetailNative 메서드를 호출
        return weaponRepository.findHelicopterDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 헬리콥터 상세 스펙을 찾을 수 없습니다."
                ));
    }

    // 전투기 상세 스펙 조회 메서드
    public Map<String, Object> getFighterDetailNative(Long weaponId) {
        return weaponRepository.findFighterDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 전투기 상세 스펙을 찾을 수 없습니다."
                ));
    }

    // 자주포
    public Map<String, Object> getArtilleryDetailNative(Long weaponId) {
        return weaponRepository.findArtilleryDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 자주포 상세 스펙을 찾을 수 없습니다."
                ));
    }


    // 탱크
    public Map<String, Object> getTankDetailNative(Long weaponId) {
        return weaponRepository.findTankDetailNative(weaponId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        weaponId + "번 ID를 가진 전차 상세 스펙을 찾을 수 없습니다."
                ));
    }

    /**
     * 특정 무기 ID의 모든 후속 모델을 재귀 쿼리로 조회합니다.
     */
    public List<Map<String, Object>> getSuccessors(Long weaponId) {
        // 💡 Optional을 사용하지 않고 빈 리스트를 반환하도록 처리
        // 데이터가 없을 경우 빈 리스트를 반환하여 Controller에서 리스트의 size로 처리합니다.
        return weaponRepository.findSuccessorsNative(weaponId);
    }
    /**
     * 특정 무기 ID의 모든 선행 모델(이전 버전)을 재귀 쿼리로 조회합니다.
     */
    public List<Map<String, Object>> getPredecessors(Long weaponId) {
        // 💡 Repository의 findPredecessorsNative 메서드를 호출
        return weaponRepository.findPredecessorsNative(weaponId);
    }

    /**
     * 특정 무기의 모든 프라모델 링크 목록을 조회합니다.
     */
    public List<Map<String, Object>> getModelLinks(Long weaponId) {
        // 💡 findModelLinksByWeaponId 호출
        return weaponRepository.findModelLinksByWeaponId(weaponId);
    }


    /**
     * 무기 데이터를 이름 문자열로 받아 DB에 저장하는 메서드
     */
    public Weapon insertNewWeapon(
            String weaponName,
            String manufacturerName,
            String categoryName,
            String platformName,
            Long baseSpecId // BaseSpec은 ID로 참조한다고 가정
    ) {

        // 1. 이름 문자열로 외래 키 객체 조회
        // 조회에 실패하면 RuntimeException을 발생시켜 트랜잭션을 롤백합니다.
        Manufacturer manufacturer = manufacturerRepository.findByName(manufacturerName)
                .orElseThrow(() -> new IllegalArgumentException("제조사 이름 '" + manufacturerName + "'를 찾을 수 없습니다."));

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 이름 '" + categoryName + "'를 찾을 수 없습니다."));

        Platform platform = platformRepository.findByName(platformName)
                .orElseThrow(() -> new IllegalArgumentException("플랫폼 이름 '" + platformName + "'를 찾을 수 없습니다."));

        // BaseSpec은 1:1 관계이며, ID가 필요하다고 가정합니다.
        BaseSpec baseSpec = baseSpecRepository.findById(baseSpecId)
                .orElseThrow(() -> new IllegalArgumentException("베이스 스펙 ID '" + baseSpecId + "'를 찾을 수 없습니다."));


        // 2. Weapon 엔티티 생성 및 값 설정
        Weapon newWeapon = new Weapon();
        newWeapon.setName(weaponName);

        // 3. 외래 키 객체를 직접 설정 (ID는 JPA가 처리)
        newWeapon.setManufacturer(manufacturer);
        newWeapon.setCategory(category);
        newWeapon.setPlatform(platform);
        //newWeapon.setBaseSpec(baseSpec);
        // previousModel은 NULL이므로 설정 생략

        // 4. DB에 저장
        return weaponRepository.save(newWeapon);
    }




}
