# 📌 API 명세서

---

## ✅ 결제 API

- 설명: 이 API는 생성된 주문에 대해 사용자 잔액으로 결제를 수행합니다.

### 🔗 Endpoint

- **URL**: `/api/v1/payment`
- **Method**: `POST`
- **headers**: 
  - `transactionId`: 고유한 트랜잭션 ID (중복방지) 
---

### 📥 Request Body

| 필드명   | 타입     | 필수 | 설명                      |
|----------|----------|------|---------------------------|
| orderId  | string   | ✅   | 결제를 수행할 주문 ID       |
| userId   | string   | ✅   | 결제 사용자 ID              |

- **Request Example**:

```json
{
  "orderId": "ORD-202507160001",
  "userId": "sampleUserId"
}
```

---

### 📤 Response Body (200 OK)

| 필드명            | 타입   | 설명                     |
|----------------|--------|------------------------|
| code           | int    | HTTP 상태 코드             |
| message        | string | 처리 결과 메시지              |
| data.paymentId | string | 결제 ID                  |
| data.userId    | string | 유저 ID                  |
| data.orderId   | string | 주문 ID                  |
| data.price     | int  | 결제 금액 (할인 적용 후)        |

```json
{
  "code": 200,
  "message": "결제 완료",
  "data": {
    "orderId": "ORD-202507160001",
    "paidAmount": 28000,
    "balance": 72000
  }
}
```

---

### ⚠️ Response (오류 예시)

#### 400 Bad Request (요청 오류)

```json
{
  "code": 400,
  "message": "요청 데이터가 올바르지 않습니다"
}
```

#### 404 Not Found (주문 또는 사용자 없음)

```json
{
  "code": 404,
  "message": "해당 주문 또는 사용자를 찾을 수 없습니다"
}
```

#### 409 Conflict (이미 결제됨)

```json
{
  "code": 409,
  "message": "해당 주문은 이미 결제 완료 상태입니다"
}
```

#### 409 Conflict (잔액 부족)

```json
{
  "code": 409,
  "message": "잔액이 부족하여 결제를 처리할 수 없습니다"
}
```

#### 500 Internal Server Error

```json
{
  "code": 500,
    "message": "서버 내부 오류"
}
```
### 📘 응답 코드 설명
| 코드 | 설명                       |
|------|--------------------------|
| 200  | 결제가 성공적으로 처리됨            |
| 400  | 요청 데이터 누락 또는 형식 오류       |
| 404  | 주문 ID 또는 사용자 ID가 존재하지 않음 |
| 409  | 잔액 부족, 중복 결제 등 충돌 상황     |
| 500  | 서버 내부 오류      |
