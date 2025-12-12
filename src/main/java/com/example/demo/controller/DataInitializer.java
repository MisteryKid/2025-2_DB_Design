package com.example.demo.controller;


import com.example.demo.repository.WeaponRepository;
import com.example.demo.service.WeaponService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final WeaponService weaponService;
    private final WeaponRepository weaponRepository; // Repository 추가 주입

    public DataInitializer(WeaponService weaponService, WeaponRepository weaponRepository) {
        this.weaponService = weaponService;
        this.weaponRepository = weaponRepository;
    }

    @Override
    public void run(String... args) throws Exception {

//        // 🚨 핵심: Weapon 테이블에 저장된 레코드 수를 확인합니다.
//        if (weaponRepository.count() > 0) {
//            System.out.println("✅ [초기 데이터] 이미 데이터가 존재하므로 삽입을 건너뜁니다.");
//            return; // 데이터가 있으면 즉시 메서드 종료
//        }
//
//        System.out.println("--- [초기 데이터] 데이터베이스가 비어있어 무기 데이터 로딩 시작 ---");
//
//        try {
//            // 1. K2 흑표 전차
//            weaponService.insertNewWeapon(
//                    "K2 흑표 전차",
//                    "현대로템",
//                    "전차",
//                    "지상",
//                    1L
//            );
//
//            // 2. KF-21 보라매
//            weaponService.insertNewWeapon(
//                    "KF-21 보라매",
//                    "한국항공우주산업 (KAI)",
//                    "전투기",
//                    "공중",
//                    2L
//            );
//
//            // 3. K9 자주포
//            weaponService.insertNewWeapon(
//                    "K9 자주포",
//                    "한화에어로스페이스",
//                    "자주포",
//                    "지상",
//                    3L
//            );
//
//            System.out.println("--- [초기 데이터] 무기 데이터 로딩 완료 (총 " + weaponRepository.count() + "개) ---");
//
//        } catch (IllegalArgumentException e) {
//            // 외래 키 데이터(제조사, 카테고리 등)가 없을 때 발생하는 예외 처리
//            System.err.println("❌ [초기 데이터] 외래 키 참조 오류 발생: " + e.getMessage());
//            System.err.println("❗ Weapon 데이터를 삽입하기 전에 Manufacturer, Category 등의 데이터를 먼저 확인하고 삽입해야 합니다.");
//        }
    }
}
