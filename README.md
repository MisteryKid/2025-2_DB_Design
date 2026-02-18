# Military Weapon & Plastic Model DB System
무기 상세 정보 및 프라모델 구매 정보 통합 제공 웹 서비스 > 동국대학교 데이터베이스 설계 프로젝트 

- 수업: 데이터베이스 설계(2025-2)
- 학과: 컴퓨터공학전공
- 학년: 3학년
- 이름: 김채원
- 개발 인원: 1인 (Full Stack)

# 프로젝트 소개
본 프로젝트는 밀리터리 마니아들을 위해 특정 무기(Weapon)의 상세 스펙 정보와 그에 해당하는 프라모델 구매처를 통합적으로 제공하는 웹 서비스입니다.
복잡한 무기 데이터(함정, 전차, 전투기 등)의 관계형 데이터베이스 설계를 기반으로, Spring Boot 3와 MariaDB를 활용하여 백엔드를 구축했습니다.

특히, 단순한 CRUD를 넘어 **재귀 쿼리**(WITH RECURSIVE)를 활용한 무기 계보(선대/후속 모델) 추적 기능을 구현하여 데이터베이스 활용 능력을 극대화했습니다.

Demo Video: YouTube Link

Backend
- Java 17
- Spring Boot 3.0
- Spring Data JPA
- MariaDB (Local)

Frontend
- HTML5 / CSS3
- Thymeleaf

Tools
- IntelliJ IDEA
- Git / GitHub

본 프로젝트는 Layered Architecture (Controller - Service - Repository)를 준수하여 유지보수성과 확장성을 고려했습니다. 
- Entity Mapping: DB 테이블 구조를 객체로 매핑하여 관리

- Hybrid Data Access:
      - Spring Data JPA: 단순 조회 및 기본 CRUD 처리 
      - Native Query (SQL): 복잡한 JOIN, 동적 스펙 조회, 재귀 쿼리(계보 추적) 처리

Key Features (핵심 기능)
1. 통합 검색 시스템 (Search)
- 카테고리 검색: 대분류(지상, 해상 등) 및 소분류(전차, 자주포 등) 기반 필터링 
- 키워드 검색: 무기 이름에 포함된 키워드 검색 (대소문자 무시 IgnoreCase) 
- 로직: findByCategory_IdIn과 findByNameContainingIgnoreCase 메서드를 활용하여 유연한 검색 지원

2. 동적 스펙 조회 (Dynamic Specification) 
- 무기 종류(함정, 잠수함, 탱크, 전투기 등)에 따라 서로 다른 상세 스펙 테이블을 가집니다.
- 구현: * Category ID를 기반으로 서비스 레이어에서 분기 처리 (Switch/If문)
- Native Query를 사용하여 각 무기 타입에 맞는 전용 테이블(Join)에서 데이터를 조회 후 반환

3. 무기 계보 추적 (Genealogy Tracking) ⭐ 
- 기능: 특정 무기의 **이전 모델(Predecessor)**과 **후속 모델(Successor)**을 계층적으로 시각화합니다.
- 기술: MariaDB의 WITH RECURSIVE 구문을 Native Query로 작성하여, 무한히 연결된 개발/개량 역사를 한 번의 쿼리로 효율적으로 조회했습니다.

4. 프라모델 구매 정보 연동 
- 무기 상세 페이지에서 해당 무기의 프라모델 상품 정보(이미지, 시리즈명, 구매 링크)를 제공합니다.
- 1:N 관계를 활용하여 하나의 무기에 여러 제조사의 프라모델 정보를 매핑했습니다.

Trouble Shooting & Limitations
1. 동적 쿼리의 유연성 부족 
- 문제: 무기 카테고리가 추가될 때마다 Controller/Service 단의 if-else 분기문과 전용 Native Query를 수정해야 함.
- 해결 방안(Future Work): QueryDSL을 도입하거나 다형성(Polymorphism)을 활용한 설계로 리팩토링하여 확장성 개선 필요.

2. 프론트엔드 템플릿 중복 
- 문제: 무기 타입별로 상세 페이지 HTML(Tank-detail.html, Vessel-detail.html 등)을 별도로 구현하여 코드 중복 발생.
- 해결 방안: Thymeleaf의 Fragment 기능을 활용하여 공통 레이아웃을 모듈화하고, 동적 폼 생성 방식으로 개선 필요.

Review
본 프로젝트를 통해 Spring Boot와 JPA의 편리함뿐만 아니라, 복잡한 데이터 관계를 풀기 위한 Native SQL의 중요성을 깊이 체감했습니다. 특히 수업 시간에 배운 **재귀 쿼리**(WITH RECURSIVE)를 실제 서비스 로직(무기 계보)에 적용해 보며, 계층형 데이터를 처리하는 DB 기술을 내재화할 수 있었습니다.





