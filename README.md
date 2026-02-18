# Military Weapon & Plastic Model DB System
무기 상세 정보 및 프라모델 구매 정보 통합 제공 웹 서비스 > 동국대학교 데이터베이스 설계 프로젝트 

- 수업: 데이터베이스 설계(2025-2)
- 학과: 컴퓨터공학전공
- 학년: 3학년
- 이름: 김채원
- 개발 인원: 1인 (Full Stack)
- 개발 기간: 약 60일

# 프로젝트 소개
 본 프로젝트는 밀리터리 마니아들을 위해 특정 무기(Weapon)의 상세 스펙 정보와 그에 해당하는 프라모델 구매처를 통합적으로 제공하는 웹 서비스이다. 
복잡한 무기 데이터(함정, 전차, 전투기 등)의 관계형 데이터베이스 설계를 기반으로, Spring Boot 3와 MariaDB를 활용하여 백엔드를 구축했다. 

특히, 단순한 CRUD를 넘어 **재귀 쿼리**(WITH RECURSIVE)를 활용한 무기 계보(선대/후속 모델) 추적 기능을 구현하여 데이터베이스 활용 능력을 극대화했다. 

Demo Video: [시연 영상](https://youtu.be/bea_D4kw5WE)

### 개발 환경 및 주요 개발 스택 
- Backend
      - Java 17
      - Spring Boot 3.0
      - Spring Data JPA
      - MariaDB (Local)
- Frontend
      - HTML5 / CSS3
      - Thymeleaf
- Tools
      - IntelliJ IDEA
      - Git / GitHub

본 프로젝트는 Layered Architecture (Controller - Service - Repository)를 준수하여 유지보수성과 확장성을 고려했다.  
      - Entity Mapping: DB 테이블 구조를 객체로 매핑하여 관리
      - Hybrid Data Access:
            - Spring Data JPA: 단순 조회 및 기본 CRUD 처리 
            - Native Query (SQL): 복잡한 JOIN, 동적 스펙 조회, 재귀 쿼리(계보 추적) 처리

---
# 1차 과제 - 주제 선정 및 ERD 구성 
### Entity Relationship Diagram (ERD)
체 데이터베이스의 구조와 엔티티 간의 관계를 시각화한 ERD이다. 
무기(Weapon)를 중심으로 각 스펙 테이블과 프라모델 정보가 유기적으로 연결되어 있다. 
<img width="1274" height="988" alt="image" src="https://github.com/user-attachments/assets/8f250642-acae-4cab-9258-5a2d487c378b" />
<br>

# 2차 과제 - DB 구축 
### 효율적인 무기 데이터 저장 전략
무기(Weapon) 데이터는 '지상/해상/공중' 등 운용 환경에 따라 **세부 스펙(속성)이 완전히 상이**.
이를 효율적으로 관리하기 위해 **계층적 설계(Supertype/Subtype)** 방식을 적용

**[문제 인식: 단일 테이블 전략의 한계]**
* **메모리 낭비 (Sparsity):** 모든 무기의 속성을 하나의 테이블에 몰아넣을 경우, 해당 무기와 관련 없는 컬럼에는 수많은 `NULL` 값이 발생하여 저장 공간이 낭비
* **데이터 무결성 위협:** 헬리콥터 데이터 입력 시 실수로 전투기 전용 속성(예: 마하 속도)에 값을 넣는 등 데이터 오염 가능성이 존재

**[해결: 상속(Inheritance) 개념을 적용한 계층적 설계]**
* **공통 속성 분리:** 모든 무기가 공통적으로 가지는 속성(이름, 제조사, 이미지 등)은 상위 테이블인 `Weapon`에서 관리
* **고유 속성 분리:** 함정, 전차, 전투기 등 각 무기 체계별 고유 스펙은 하위 테이블로 분리하여 **1:1 관계**로 매핑
* **효과:** 데이터 중복을 최소화하고 메모리 효율성을 극대화했으며, 각 무기 타입에 맞는 데이터만 입력되도록 강제하여 무결성을 확보.
<img width="1162" height="452" alt="image" src="https://github.com/user-attachments/assets/6257d8a0-e2f1-432e-a062-a24b713b418d" />


# 3차 과제 - 웹 구현 

Key Features (핵심 기능)
1. 통합 검색 시스템 (Search)
      - 카테고리 검색: 대분류(지상, 해상 등) 및 소분류(전차, 자주포 등) 기반 필터링 
      - 키워드 검색: 무기 이름에 포함된 키워드 검색 (대소문자 무시 IgnoreCase) 
      - 로직: findByCategory_IdIn과 findByNameContainingIgnoreCase 메서드를 활용하여 유연한 검색 지원
```
findByNameContaining(String name) # 무기 이름에 특정 키워드가 포함된 항목을 조회
findByCategory_IdIn(Collection<Long> categoryIds) # 카테고리 ID 목록을 이용한 다중 카테고리 검색을 지원
findByNameContainingIgnoreCase(String name) # 대소문자를 구분하지 않는 이름 검색을 구현하여 사용자 편의성을 증진
```
2. 동적 스펙 조회 (Dynamic Specification) 
      - 무기 종류(함정, 잠수함, 탱크, 전투기 등)에 따라 서로 다른 상세 스펙 테이블을 가짐
      - 구현: * Category ID를 기반으로 서비스 레이어에서 분기 처리 (Switch/If문)
      - Native Query를 사용하여 각 무기 타입에 맞는 전용 테이블(Join)에서 데이터를 조회 후 반환
```
@Query(value ="SELECT " +"    W.name AS weapon_name, " +"    W.weapon_id, " +"    SS.max_speed_knot, " +"    SS.displacement, " +"    SS.crew_capacity AS sea_crew_capacity, " +"    SS.armament, " +"    SS.radar_type, " +"    V.accuracy_speed, " +"    V.max_speed_vessel, " +"    V.buoyancy_vessel " +"FROM " +"    Weapon W " +"JOIN " +"    sea_spec SS ON W.weapon_id = SS.weapon_weapon_id " +"JOIN " +"    vessel V ON SS.weapon_weapon_id = V.weapon_id " +"WHERE " +"    W.weapon_id = :weaponId",nativeQuery = true)Optional<Map<String, Object>> findVesselDetailNative(@Param("weaponId") Long weaponId);
```

3. 무기 계보 추적 (Genealogy Tracking)
      - 기능: 특정 무기의 **이전 모델(Predecessor)**과 **후속 모델(Successor)**을 계층적으로 시각화합니다.
      - 기술: MariaDB의 WITH RECURSIVE 구문을 Native Query로 작성하여, 무한히 연결된 개발/개량 역사를 한 번의 쿼리로 효율적으로 조회함
```sql
WITH RECURSIVE successor_chain AS (
    -- Anchor Member: 시작점 (직계 후속 모델 찾기)
    SELECT W.weapon_id, W.name, W.previous_model_id, 1 AS generation_level
    FROM weapon W
    WHERE W.previous_model_id = :startWeaponId

    UNION ALL

    -- Recursive Member: 꼬리에 꼬리를 무는 재귀 탐색
    SELECT W.weapon_id, W.name, W.previous_model_id, SC.generation_level + 1
    FROM weapon W
    INNER JOIN successor_chain SC ON W.previous_model_id = SC.weapon_id
)
SELECT 
    T.weapon_id, 
    T.name AS successor_name, 
    T.generation_level,
    (SELECT name FROM weapon WHERE weapon_id = T.previous_model_id) AS previous_model_name
FROM successor_chain T
ORDER BY T.generation_level ASC;



WITH RECURSIVE predecessor_chain AS (
    -- Anchor Member: 시작점 (현재 무기)
    SELECT W.weapon_id, W.name, W.previous_model_id, 0 AS generation_level
    FROM weapon W
    WHERE W.weapon_id = :startWeaponId

    UNION ALL

    -- Recursive Member: 이전 모델(previous_model_id)을 타고 역방향 탐색
    SELECT P.weapon_id, P.name, P.previous_model_id, PC.generation_level + 1
    FROM weapon P
    INNER JOIN predecessor_chain PC ON P.weapon_id = PC.previous_model_id
)
SELECT 
    T.weapon_id, 
    T.name AS predecessor_name, 
    T.generation_level,
    (SELECT name FROM weapon WHERE weapon_id = T.previous_model_id) AS previous_model_name
FROM predecessor_chain T
WHERE T.generation_level > 0 -- 자기 자신 제외
ORDER BY T.generation_level DESC; -- 가장 오래된 모델부터 정렬
```


4. 프라모델 구매 정보 연동 
      - 무기 상세 페이지에서 해당 무기의 프라모델 상품 정보(이미지, 시리즈명, 구매 링크)를 제공합니다.
      - 1:N 관계를 활용하여 하나의 무기에 여러 제조사의 프라모델 정보를 매핑함
```
@Query(value ="SELECT " +"    ML.id, " + // model_link의 고유ID (필요한 경우)"    ML.product_page_url, " + // 프라모델 판매 링크"    ML.series, " +          // 무기 시리즈 명"    ML.image_url, " +        // 이미지URL"    ML.description, " +      // 상세 설명"    ML.name "+"FROM " +"    model_link ML " +"WHERE " +"    ML.weapon = :weaponId", // Weapon ID로 필터링nativeQuery = true)List<Map<String, Object>> findModelLinksByWeaponId(@Param("weaponId") Long weaponId);

```

## 구현 결과
 
### 메인화면
 맨 처음 화면은 모든 무기 리스트를 보여준다. 해당 화면에서 카테고리별로 무기를 볼 수 있고 검색 또한 가능하다. 특정 무기의 정보를 더 자세히 보고 싶다면 해당 무기의 열을 클릭하여 상세 페이지로 이동이 가능하다. 
<img width="569" height="285" alt="image" src="https://github.com/user-attachments/assets/9ad18111-39e4-4a5f-92b9-fa2f43a17646" />

### 검색 기능 
1. 주요 카테고리에서 “지상무기”를 택했을 시 지상무기인 “전차”, “자주포”를 카테고리값으로 가진 무기 리스트가 출력되는 것을 볼 수 있다. 
<img width="569" height="281" alt="image" src="https://github.com/user-attachments/assets/8ee4b821-1622-456c-90f8-3dcb40baf15b" />

2. 주요 카테고리를 선택 시 세부 카테고리를 이어서 선택할 수 있다. 지상무기를 주요 카테고리로서 선택했을 때 세부카테고리에는 “전차” 혹은 “자주포”라는 선택지를 고를 수 있다. 아래의 사진은 세부 카테고리로 “전차”를 골랐을 때의 모습이며 데이터베이스에 저장되어있는 모든 전차 무기를 SELECT해 온 것을 볼 수 있다. 
<img width="569" height="269" alt="image" src="https://github.com/user-attachments/assets/8ca33a51-4f2b-4aae-b977-60d03ea75bc4" />

3. 카테고리를 선택하지 않고 검색을 통해서 무기를 검색할 수 있다. 만약 “보”라는 글자를 검색하면 해당 글자가 들어간 모든 무기, 아래에서는 “KF-21 보라매”와 “K-21 보병전투차”가 검색된 것을 볼 수 있다. 해당 기능을 이용해 특정 키워드가 포함된 무기를 대소문자 구분 없이 조회가 가능하다. 
<img width="569" height="189" alt="image" src="https://github.com/user-attachments/assets/04bd2b13-2c0f-4352-9f65-a3226387c0bf" />

### 세부 화면
 main화면에서 특정 무기의 행을 눌렀을 때 이동할 수 있는 페이지이다. 해당 페이지에서는 특정 무기에 대한 세부 스펙 정보와 프라모델 링크를 볼 수 있다. 예를 들어서 무기의 스펙 정보, 개량 버전, 이전 버전, 프라모델 링크 정보를 볼 수 있다. 아래의 페이지는 main화면에서 K2 픅표 전차를 선택했을 때 보이는 화면이다. 
<img width="422" height="641" alt="image" src="https://github.com/user-attachments/assets/2836e7a7-4029-487b-9bfb-2d8d899e97d8" />

 K 시리즈 전차의 경우 K-1, K-1A1, K-1A2 순으로 개량했는데 이 순서대로 이전모델, 후속 모델이 채워진 것을 볼 수 있다. 
<img width="548" height="366" alt="image" src="https://github.com/user-attachments/assets/9285d581-ebe6-480d-8bac-3c8b15835b18" />

K-1의 경우 후속모델이 K-1A1, K-1A2가 들어있다. 
K-1A1은 이전모델에 K-1, 후속모델에 K-1A2가 들어있고 K-1A2는 이전모델에 K-1. K-1A1전차가 들어있는 것을 볼 수 있다. 

## 결론 
- 프로젝트 요약 및 성과
 본 프로젝트는 Spring Boot 3를 기반으로 무기 정보 조회 및 검색 기능을 갖춘 웹사이트를 성공적으로 구현했다.

- 목표 달성
 무기의 카테고리 및 키워드 통합 검색, 상세 스펙 조회, 계보 추적(선대/후속 모델), 그리고 프라모델 구매 링크 연동이라는 핵심 목표를 모두 달성했다. 

- 아키텍처 성과
계층 구조: Controller, Service, Repository로 명확하게 계층을 분리하는 Layered Architecture를 적용하여 코드의 유지보수성과 확장성을 높였다. 

- DB 활용
 재귀 쿼리(WITH RECURSIVE)와 같은 복잡한 스펙 조회에는 Native Query (SQL)를, 기본적인 CRUD 및 단순 검색에는 Spring Data JPA Query Method를 혼합 사용하여 데이터 접근의 효율성과 생산성을 동시에 확보했다.

## Trouble Shooting & Limitations
      1. 동적 쿼리의 유연성 부족 
      - 문제: 무기 카테고리가 추가될 때마다 Controller/Service 단의 if-else 분기문과 전용 Native Query를 수정해야 함.
      - 해결 방안(Future Work): QueryDSL을 도입하거나 다형성(Polymorphism)을 활용한 설계로 리팩토링하여 확장성 개선 필요.
      
      2. 프론트엔드 템플릿 중복 
      - 문제: 무기 타입별로 상세 페이지 HTML(Tank-detail.html, Vessel-detail.html 등)을 별도로 구현하여 코드 중복 발생.
      - 해결 방안: Thymeleaf의 Fragment 기능을 활용하여 공통 레이아웃을 모듈화하고, 동적 폼 생성 방식으로 개선 필요.

Review
      본 프로젝트를 통해 Spring Boot와 JPA의 편리함뿐만 아니라, 복잡한 데이터 관계를 풀기 위한 Native SQL의 중요성을 깊이 체감했습니다. 특히 수업 시간에 배운 **재귀 쿼리**(WITH RECURSIVE)를 실제 서비스 로직(무기 계보)에 적용해 보며, 계층형 데이터를 처리하는 DB 기술을 내재화할 수 있었습니다.





