# 📌 API 명세서

---

## ✅ 주문 생성 API

- 설명: 사용자와 상품 정보를 기반으로 주문을 생성하고 저장합니다.  

### 🔗 Endpoint

- **URL**: `/api/v1/order`
- **Method**: `POST`

---

### 📥 Request Body

| 필드명               | 타입     | 필수 | 설명                            |
|-------------------|----------|------|-------------------------------|
| userId            | string   | ✅   | 주문자 ID                        |
| userCouponId      | string   | ❌   | 사용자 보유 쿠폰 ID (사용 안 할 경우 null) |
| items             | array    | ✅   | 주문 상품 목록                      |
| items[].productId | string | ✅ | 상품 ID                         |
| items[].price     | int    | ✅ | 가격                            |
| items[].quantity  | int    | ✅ | 수량                            |

- **Request Example**:

```json
{
  "userId": "sampleUserId",
  "userCouponId": "hanghae99",
  "items": [
    {
      "productId": "P001",
      "quantity": 2
    },
    {
      "productId": "P003",
      "quantity": 1
    }
  ]
}
```

---

### 📤 Response Body (201 Created)

| 필드명             | 타입   | 설명        |
|-----------------|--------|-----------|
| code            | int    | HTTP 상태 코드 |
| message         | string | 처리 결과 메시지 |
| data.orderId    | string | 생성된 주문 ID |
| data.userId     | string | 주문한 유저 ID |
| data.status     | string | 초기 주문 상태  |
| data.totalPrice | int | 주문 총 금액   |
| data.orderDate  | string | 주문 생성 시각  |

```json
{
  "code": 201,
  "message": "주문 생성 완료",
  "data": {
    "orderId": "ORD-202507160001",
    "userId": "sampleUserId",
    "status": "PENDING",
    "totalPrice": 10000,
    "orderDate": "2025-07-16T12:05:00"
  }
}
```

---

### ⚠️ Response (오류 예시)

#### 400 Bad Request

```json
{
  "code": 400,
  "message": "요청 데이터가 올바르지 않습니다"
}
```

#### 404 Not Found (상품/사용자 없음)

```json
{
  "code": 404,
  "message": "존재하지 않는 사용자 또는 상품입니다"
}
```

#### 409 Conflict (재고 부족)

```json
{
  "code": 409,
  "message": "상품 재고가 부족합니다"
}
```

#### 500 Internal Server Error

```json
{
  "code": 500,
  "message": "서버 내부 오류"
}
```

---

### 📘 응답 코드 설명

| 코드 | 설명                  |
|------|---------------------|
| 201  | 주문이 성공적으로 생성됨       |
| 400  | 필수 데이터 누락 또는 형식 오류  |
| 404  | 존재하지 않는 사용자 또는 상품 ID |
| 409  | 재고 부족 등으로 주문 생성 불가  |
| 500  | 서버 내부 오류  |
