DELETE FROM balance;
DELETE FROM coupon;

-- 잔액 샘플 데이터
INSERT INTO balance (user_id, balance) VALUES ('sampleUserId', 100000);

-- 쿠폰 샘플 데이터
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (1, '깜짝 10% 할인쿠폰', 10, 'AVAILABLE', 0.1);

