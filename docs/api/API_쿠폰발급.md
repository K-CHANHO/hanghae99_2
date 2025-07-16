# 📌 API 명세서

---

## ✅ 쿠폰 발급 API

- 설명: 이 API는 사용자가 선착순으로 쿠폰을 발급받는 기능을 수행합니다.

### 🔗 Endpoint

- **URL**: `/api/v1/coupon`
- **Method**: `POST`
- **headers**: 
  - `transactionId`: 고유한 트랜잭션 ID (중복방지)

---

### 📥 Request Body

| 필드명   | 타입   | 필수 | 설명                     |
|----------|--------|------|--------------------------|
| userId   | string | ✅   | 쿠폰을 발급받을 사용자 ID |
| couponId | string | ✅   | 발급받을 쿠폰 ID          |

- **Request Example**:

```json
{
  "userId": "sampleUserId",
  "couponId": "hanghae99"
}
```

---

### 📤 Response Body (200 OK)

| 필드명                 | 타입    | 설명            |
|---------------------|---------|---------------|
| code                | int     | HTTP 상태 코드    |
| message             | string  | 처리 결과 메시지     |
| data.couponId       | string  | 발급된 쿠폰 코드 (고유 ID) |
| data.ruleId         | string  | 쿠폰 정책 ID      |
| data.couponName     | string  | 쿠폰 이름         |
| data.status         | string  | 쿠폰 상태(발급중, 발급중지) |
| data.totalQuantity | int     | 총 발급 수량       |
| data.remainQuantity | int     | 현재 발급 가능한 수량  |

```json
{
  "code": 200,
  "message": "쿠폰 발급 성공",
  "data": {
    "couponId": "hanghae99",
    "ruleId": "CPN-1a2b3c4d",
    "couponName": "항해99 백엔드 9기 쿠폰",
    "status": "발급중",
    "totalQuantity": 100,
    "remainQuantity": 20
  }
}
```

---

### ⚠️ Response (오류 예시)

#### 400 Bad Request

```json
{
  "code": 400,
  "message": "요청 파라미터가 잘못되었습니다"
}
```

#### 409 Conflict (쿠폰 소진)

```json
{
  "code": 409,
  "message": "쿠폰이 모두 소진되었습니다"
}
```

#### 409 Conflict (중복 발급)

```json
{
  "code": 409,
  "message": "이미 쿠폰을 발급받았습니다"
}
```

#### 404 Not Found

```json
{
  "code": 404,
  "message": "존재하지 않는 쿠폰입니다"
}
```

---

### 📘 응답 코드 설명

| 코드 | 설명                        |
|------|---------------------------|
| 200  | 성공적으로 쿠폰 발급               |
| 400  | 필수 파라미터 누락 또는 형식 오류       |
| 404  | couponId에 해당하는 쿠폰이 존재하지 않음 |
| 409  | 중복 발급 또는 쿠폰 수량 초과         |
| 500  | 서버 내부 오류 |

---

### 📎 비고

- 쿠폰 발급은 선착순이며, 중복 발급 불가
- 동시에 여러 요청이 몰릴 수 있으므로 Redis Lock, DB Lock 등으로 동시성 제어 필요
- 발급된 쿠폰은 이후 주문 시 사용 가능

