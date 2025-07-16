# 📌 API 명세서

---

## ✅ 잔액 조회 API

- 설명: 이 API는 사용자 잔액을 조회하는 기능을 수행합니다.

### 🔗 Endpoint

- **URL**: `/api/v1/balance/{userId}`
- **Method**: `GET`

---

### 📥 Path Parameter

| 파라미터명 | 타입   | 필수 | 설명                     |
|------------|--------|------|--------------------------|
| userId     | string | ✅   | 잔액을 조회할 사용자 ID |

- **Request Example**:
  ```
  /v1/balance?userId=sampleUserId
  ```

---

### 📤 Response Body (200 OK)

| 필드명         | 타입   | 설명               |
|----------------|--------|--------------------|
| code           | int    | HTTP 상태 코드     |
| message        | string | 처리 결과 메시지   |
| data.userId    | string | 사용자 ID          |
| data.balance   | int    | 현재 잔액 (원 단위) |

```json
{
  "code": 200,
  "message": "잔액 조회 성공",
  "data": {
    "userId": "sampleUserId",
    "balance": 120000
  }
}
```

---

### ⚠️ Response (오류 예시)

#### 400 Bad Request

```json
{
  "code": 400,
  "message": "userId 파라미터는 필수입니다"
}
```

#### 404 Not Found

```json
{
  "code": 404,
  "message": "해당 사용자를 찾을 수 없습니다"
}
```

---

### 📘 응답 코드 설명

| 코드 | 설명                              |
|------|-----------------------------------|
| 200  | 성공적으로 잔액 조회              |
| 400  | userId 누락 또는 형식 오류         |
| 404  | 해당 userId에 대한 사용자 없음     |
| 500  | 서버 오류                         |
