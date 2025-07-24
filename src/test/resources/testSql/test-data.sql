DELETE FROM balance;
DELETE FROM coupon;
DELETE FROM user_coupon;
DELETE FROM product;
DELETE FROM product_stock;

-- 잔액 샘플 데이터
INSERT INTO balance (user_id, balance) VALUES ('sampleUserId', 150000);

-- 쿠폰 샘플 데이터
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (1, '깜짝 10% 할인쿠폰', 10, 'AVAILABLE', 0.1);
INSERT INTO coupon (coupon_id, coupon_name, quantity, status, discount_rate) VALUES (2, '깜짝 20% 할인쿠폰', 20, 'AVAILABLE', 0.2);

-- 유저 쿠폰 샘플 데이터
INSERT INTO user_coupon (user_coupon_id, user_id, coupon_id, status, issued_at, expired_at) VALUES (1, 'sampleUserId', 1, 'AVAILABLE', '2025-07-25 00:50:00', '2025-08-01 00:50:00');

-- 상품 샘플 데이터
INSERT INTO product (product_id, product_name, price) VALUES (1, '샘플 상품 1', 10000);
INSERT INTO product (product_id, product_name, price) VALUES (2, '샘플 상품 2', 20000);
INSERT INTO product (product_id, product_name, price) VALUES (3, '샘플 상품 3', 30000);

-- 상품 재고 샘플 데이터
INSERT INTO product_stock (product_id, stock_quantity) VALUES (1, 49);
INSERT INTO product_stock (product_id, stock_quantity) VALUES (2, 20);
INSERT INTO product_stock (product_id, stock_quantity) VALUES (3, 30);

