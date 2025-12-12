-- 1. 기초 참조 데이터 (국가, 제조사, 상태, 플랫폼)
-- ID를 명시적으로 지정하여 나중에 헷갈리지 않게 합니다.

INSERT INTO country (country_id, name, description) VALUES (1, '한국', '대한민국');
INSERT INTO country (country_id, name, description) VALUES (2, '미국', '미합중국');

INSERT INTO manufacturer (manufacturer_id, name, description) VALUES (1, '현대로템', '지상 무기 전문');
INSERT INTO manufacturer (manufacturer_id, name, description) VALUES (2, 'KAI', '한국항공우주산업');

INSERT INTO current_status (status_id, status) VALUES (1, '운용 중');
INSERT INTO current_status (status_id, status) VALUES (2, '개발 중');

INSERT INTO platform (platform_id, name) VALUES (1, '지상');
INSERT INTO platform (platform_id, name) VALUES (2, '공중');
INSERT INTO platform (platform_id, name) VALUES (3, '해상');

-- 2. [지상 무기] K2 흑표 전차 데이터 삽입 (ID = 1)
-- 주의: JOINED 전략이므로 3개의 테이블에 모두 insert 해야 하며, weapon_id가 1로 같아야 합니다.

-- 2-1. 부모 테이블 (weapon)
INSERT INTO weapon (weapon_id, dtype, name, image_url, country_id, manufacturer_id, status_id, platform_id)
VALUES (1, 'Tank', 'K2 흑표', 'http://example.com/k2.jpg', 1, 1, 1, 1);

-- 2-2. 중간 테이블 (land_weapon) - ID 1
INSERT INTO land_weapon (weapon_id, max_speed, crew, armor_thickness, ammo_capacity)
VALUES (1, 70.0, 3, 500.0, 40);

-- 2-3. 자식 테이블 (tank) - ID 1
INSERT INTO tank (weapon_id, weight, slope_angle)
VALUES (1, 55.0, 60.0);


-- 3. [공중 무기] KF-21 보라매 데이터 삽입 (ID = 2)

-- 3-1. 부모 테이블 (weapon)
INSERT INTO weapon (weapon_id, dtype, name, image_url, country_id, manufacturer_id, status_id, platform_id)
VALUES (2, 'FighterJet', 'KF-21 보라매', 'http://example.com/kf21.jpg', 1, 2, 2, 2);

-- 3-2. 중간 테이블 (air_weapon) - ID 2
INSERT INTO air_weapon (weapon_id, max_speed, ceiling, climb_rate, armament, crew)
VALUES (2, 1.81, 29000.0, 250.0, '미티어 미사일', 1);

-- 3-3. 자식 테이블 (fighter_jet) - ID 2
INSERT INTO fighter_jet (weapon_id, g_limit, hardpoints)
VALUES (2, 9.0, 10);


-- 4. 구매 정보 (PurchaseInfo) 연결
INSERT INTO purchase_info (model_name, link, weapon_id) VALUES ('아카데미과학 K2 프라모델', 'http://shop.com/k2', 1);
INSERT INTO purchase_info (model_name, link, weapon_id) VALUES ('아카데미과학 KF-21', 'http://shop.com/kf21', 2);