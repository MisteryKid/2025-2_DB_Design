package com.example.demo.controller;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.WeaponService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.example.demo.domain.Weapon;
import com.example.demo.repository.WeaponRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.domain.Category;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class MainController {

    private final WeaponRepository weaponRepository;
    private final CategoryRepository categoryRepository; // ⬅️ 추가
    private final WeaponService weaponService;
    private static final Long PLATFORM_ID_SEA = 3L;

    // 생성자 주입
    public MainController(WeaponRepository weaponRepository, CategoryRepository categoryRepository,
                          WeaponService weaponService) {
        this.weaponRepository = weaponRepository;
        this.categoryRepository = categoryRepository;
        this.weaponService = weaponService;
    }

    /**
     * 카테고리 또는 키워드로 무기를 검색하는 요청을 처리합니다.
     */
    @GetMapping("/main")
    public String listWeapons(Model model) {
        // 1. 모든 Weapon 엔티티를 DB에서 조회합니다.
        List<Weapon> weapons = weaponRepository.findAll();

        // 2. 조회된 목록을 "weaponList"라는 이름으로 뷰(Thymeleaf)에 전달합니다.
        model.addAttribute("weaponList", weapons);

        // 3. 템플릿 파일 이름인 "weapon-search"를 반환합니다.
        return "weapon-search";
    }

    /*
    main 페이지에서 카테고리 분류 및 검색 기능 구현
     */
    @GetMapping("/search")
    public String searchWeapons(
            // 카테고리는 Long ID로 받도록 수정 (HTML의 <option> value와 일치해야 함)
            // 세부 카테고리 ID를 명시적으로 받습니다.
            @RequestParam(name = "categoryId", required = false) Optional<Long> subCategoryId, @RequestParam(name = "keyword", required = false) String keyword,
            // 상위 카테고리 ID를 명시적으로 받습니다.
            @RequestParam(name = "mainCategoryId", required = false) Optional<Long> mainCategoryId, // ⬅️ 추가
            Model model) {

        List<Weapon> searchResults;

        // 검색할 최종 카테고리 ID를 결정합니다. 세부 카테고리 ID가 우선합니다.
        Optional<Long> searchTargetId = subCategoryId.filter(id -> id != 0) // 0이 아닌 유효한 세부 ID
                .or(() -> mainCategoryId.filter(id -> id != 0)); // 세부 ID가 없으면 상위 ID 사용

        // 1. 세부 검색 조건이 없는 경우의 categoryId를 상위 카테고리로 간주합니다.
        //    (JavaScript에서 subId가 비어있을 때 mainId로 덮어쓰므로 categoryId에는 상위 ID가 들어올 수 있음)
        // 1. 카테고리 검색 (키워드가 없는 경우)
        if (searchTargetId.isPresent() && (keyword == null || keyword.trim().isEmpty())) {
            Long finalSearchId = searchTargetId.get();

            List<Long> searchCategoryIds = new ArrayList<>();
            searchCategoryIds.add(finalSearchId);

            // 상위 ID를 부모로 갖는 모든 하위 카테고리 ID를 찾아서 추가합니다.
            // (예: finalSearchId=1(지상무기)인 경우, 하위 4(전차), 5(자주포)를 찾습니다.)
            List<Long> subIds = categoryRepository.findByParent_Id(finalSearchId)
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            searchCategoryIds.addAll(subIds);

            // ID 목록 (예: [1, 4, 5])을 사용하여 무기를 조회합니다.
            searchResults = weaponRepository.findByCategory_IdIn(searchCategoryIds);

            // 2. 키워드 검색
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            searchResults = weaponRepository.findByNameContainingIgnoreCase(keyword);

            // 3. 전체 목록 표시 (모든 조건이 없는 경우)
        } else {
            searchResults = weaponRepository.findAll(); // ⬅️ 이 블록이 실행되면 모든 무기가 보입니다.
        }

//        List<Weapon> searchResults;
//
//        // 1. 카테고리가 선택되었고 (ID가 있고) 키워드가 비어있는 경우
//        if (categoryId.isPresent() && (keyword == null || keyword.trim().isEmpty())) {
//            searchResults = weaponRepository.findByCategory_Id(categoryId.get());
//
//            // 2. 키워드만 있는 경우 (전체 카테고리에서 검색)
////        } else if (keyword != null && !keyword.trim().isEmpty()) {
////            searchResults = weaponRepository.findByNameContainingIgnoreCase(keyword);
////
////            // 3. 둘 다 없는 경우 (전체 목록 표시)
//        }
//        else {
//            searchResults = weaponRepository.findAll();
//        }

        model.addAttribute("weaponList", searchResults);

        // 검색 결과를 템플릿에 유지하기 위해, 검색 필드에 선택된 값도 모델에 다시 담아줍니다.
        model.addAttribute("selectedCategoryId", subCategoryId.orElse(null));
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("selectedMainCategoryId", mainCategoryId.orElse(null)); //

        return "weapon-search";
    }

    @GetMapping("/weapon/{weaponId}")
    public String getWeaponDetail(@PathVariable("weaponId") Long weaponId, Model model) {
        //Long platformId = weaponService.getPlatformId(weaponId);

        Long categoryId = weaponService.getCategoryId(weaponId);

        // 전투함 처리
        if (categoryId.equals(9l)) {
            Map<String, Object> weaponDetailMap = weaponService.getVesselDetailNative(weaponId);
            // 2. 조회된 Weapon 객체를 'weapon'이라는 이름으로 모델에 담아 템플릿으로 전달합니다.
            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);
            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);


            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);


            model.addAttribute("weapon", weaponDetailMap);

            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용
            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Vessel-detail";

            // 잠수정 처리
        } else if (categoryId.equals(8L)) {
            Map<String, Object> weaponDetailMap = weaponService.getSubmarineDetailNative(weaponId);

            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);
            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);

            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);

            model.addAttribute("weapon", weaponDetailMap);
            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용

            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Submarine-detail";
        } else if (categoryId.equals(7L)) {
            Map<String, Object> weaponDetailMap = weaponService.getHelicopterDetailNative(weaponId);

            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);
            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);

            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);

            model.addAttribute("weapon", weaponDetailMap);
            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용

            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Helicopter-detail";
        } else if (categoryId.equals(6L)) {
            Map<String, Object> weaponDetailMap = weaponService.getFighterDetailNative(weaponId);

            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);
            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);

            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);

            model.addAttribute("weapon", weaponDetailMap);
            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용

            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Fighter-detail";
        } else if(categoryId.equals(5L)) {
            Map<String, Object> weaponDetailMap = weaponService.getArtilleryDetailNative(weaponId);

            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);
            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);

            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);

            model.addAttribute("weapon", weaponDetailMap);
            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용

            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Artillery-detail";

        } else if (categoryId.equals(4L)) {
            Map<String, Object> weaponDetailMap = weaponService.getTankDetailNative(weaponId);

            // 💡 1. 이전 모델 목록 조회 (Service에 getPredecessors 메서드 필요)
            List<Map<String, Object>> predecessors = weaponService.getPredecessors(weaponId);
            // 💡 2. 후속 모델 목록 조회 (Service에 getSuccessors 메서드 필요)
            List<Map<String, Object>> successors = weaponService.getSuccessors(weaponId);

            List<Map<String, Object>> modelLinks = weaponService.getModelLinks(weaponId);

            model.addAttribute("predecessors", predecessors);
            model.addAttribute("successors", successors);


            model.addAttribute("weapon", weaponDetailMap);
            model.addAttribute("modelLinks", modelLinks); // 💡 새로운 속성 이름 사용

            // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
            return "Tank-detail";

        }

        return "weapon-search";
    }
}

//        Map<String, Object> weaponDetailMap = weaponService.getVesselDetailNative(weaponId);
//        // 2. 조회된 Weapon 객체를 'weapon'이라는 이름으로 모델에 담아 템플릿으로 전달합니다.
//        model.addAttribute("weapon", weaponDetailMap);
//        // 3. Thymeleaf 템플릿 파일 이름 (weapon_detail.html)을 반환합니다.
//        return "Vessel-detail";