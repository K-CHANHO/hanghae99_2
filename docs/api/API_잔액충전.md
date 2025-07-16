# 📌 API 명세서

---

## ✅ 잔액 충전 API

- 설명: 이 API는 사용자 잔액을 원하는 금액만큼 충전하는 기능을 수행합니다.

### 🔗 Endpoint

- **URL**: `/api/v1/balance`
- **Method**: `PATCH`
- **headers**: 
  - `transactionId`: 고유한 트랜잭션 ID (중복방지)

---

### 📥 Request Body

| 필드명   | 타입   | 필수 | 설명                     |
|----------|--------|------|--------------------------|
| userId   | string | ✅   | 잔액을 충전할 사용자 ID  |
| amount   | int    | ✅   | 충전할 금액 (원 단위)    |

- **Request Example**:

```json
{
  "userId": "sampleUserId",
  "amount": 50000
}
```

---

### 📤 Response Body (200 OK)

| 필드명         | 타입   | 설명               |
|----------------|--------|--------------------|
| code           | int    | HTTP 상태 코드     |
| message        | string | 처리 결과 메시지   |
| data.userId    | string | 사용자 ID          |
| data.balance   | int    | 충전 후 잔액        |

```json
{
  "code": 200,
  "message": "잔액 충전 성공",
  "data": {
    "userId": "sampleUserId",
    "balance": 170000
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

#### 404 Not Found

```json
{
  "code": 404,
  "message": "해당 사용자를 찾을 수 없습니다"
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

| 코드 | 설명                                                          |
|------|-------------------------------------------------------------|
| 200  | 성공적으로 잔액 충전                                                 |
| 400  | 요청 데이터 누락 또는 형식오류(userId 또는 amount) <br/> 또는 amount에 음수값 입력 |
| 404  | 해당 userId에 대한 사용자 없음                                        |
| 500  | 서버 오류 (예: 데이터베이스 연결 실패 등)                                   |
