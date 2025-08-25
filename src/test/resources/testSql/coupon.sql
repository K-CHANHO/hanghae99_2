-- 쿠폰 샘플 데이터
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (1, '깜짝 10% 할인쿠폰', 1, 'AVAILABLE', 0.1);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (2, '깜짝 20% 할인쿠폰', 1, 'AVAILABLE', 0.2);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (3, '깜짝 30% 할인쿠폰', 1, 'AVAILABLE', 0.3);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (4, '깜짝 40% 할인쿠폰', 1, 'AVAILABLE', 0.4);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (5, '깜짝 50% 할인쿠폰', 100, 'AVAILABLE', 0.5);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (6, '깜짝 60% 할인쿠폰', 40, 'AVAILABLE', 0.6);

-- 유저 쿠폰 샘플 데이터
INSERT INTO user_coupon (user_coupon_id, user_id, coupon_id, status, issued_at, expired_at, used_at) VALUES (1, 'sampleUserId', 1, 'AVAILABLE', '2025-07-25 00:50:00', '2030-08-01 00:50:00', NULL);
INSERT INTO user_coupon (user_coupon_id, user_id, coupon_id, status, issued_at, expired_at, used_at) VALUES (3, 'sampleUserId', 3, 'USED', '2025-07-25 00:50:00', '2030-08-01 00:50:00', NULL);
INSERT INTO user_coupon (user_coupon_id, user_id, coupon_id, status, issued_at, expired_at, used_at) VALUES (4, 'sampleUserId', 4, 'AVAILABLE', '2025-07-20 00:50:00', '2030-07-27 00:50:00', NULL);

-- 쿠폰 재고 데이터
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (1, 0);
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (2, 20);
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (3, 30);
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (4, 40);
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (5, 100);
INSERT INTO coupon_stock (coupon_id, quantity) VALUES (6, 40);