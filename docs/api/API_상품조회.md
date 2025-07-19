# 📌 API 명세서

---

## ✅ 상품 조회 API

- 설명: 이 API는 특정 상품 ID에 해당하는 상품의 정보를 조회하는 기능을 수행합니다.

### 🔗 Endpoint

- **URL**: `/v1/products/{productId}`
- **Method**: `GET`

---

### 📥 Path Parameter

| 파라미터명  | 타입   | 필수 | 설명              |
|-------------|--------|------|-------------------|
| productId   | string | ✅   | 조회할 상품의 ID  |

- **Request Example**:
  ```
  /v1/products/P001
  ```

---

### 📤 Response Body (200 OK)

| 필드명              | 타입   | 설명                  |
|------------------|--------|-----------------------|
| code             | int    | HTTP 상태 코드        |
| message          | string | 처리 결과 메시지      |
| data.productId   | string | 상품 ID               |
| data.productName | string | 상품명                |
| data.price       | int    | 상품 가격 (원 단위)   |
| data.stock       | int    | 재고 수량             |

```json
{
  "code": 200,
  "message": "상품 조회 성공",
  "data": {
    "id": "P001",
    "name": "멍멍이 영양 간식",
    "price": 12000,
    "stock": 30
  }
}
```

---

### ⚠️ Response (오류 예시)

#### 404 Not Found

```json
{
  "code": 404,
  "message": "해당 상품을 찾을 수 없습니다"
}
```

---

### 📘 응답 코드 설명

| 코드 | 설명                      |
|------|-------------------------|
| 200  | 성공적으로 상품 정보 조회          |
| 404  | 해당 상품 ID에 대한 상품이 존재하지 않음 |
| 500  | 서버 내부 오류                |
