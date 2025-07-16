# 📌 API 명세서

---

## ✅ 인기 상품 조회 API

- 설명: 이 API는 최근 판매량 기준으로 인기 상품 목록을 조회합니다.

### 🔗 Endpoint

- **URL**: `/v1/product/popular`
- **Method**: `GET`

---

- **Request Example**:
```
/v1/products/popular
```

---

### 📤 Response Body (200 OK)

| 필드명                  | 타입     | 설명                          |
|--------------------------|----------|-------------------------------|
| code                     | int      | HTTP 상태 코드                |
| message                  | string   | 처리 결과 메시지              |
| data[].productId         | string   | 상품 ID                       |
| data[].name              | string   | 상품명                        |
| data[].price             | int      | 상품 가격                     |
| data[].totalSold         | int      | 기간 내 판매 수량             |
```json
{
  "code": 200,
  "message": "인기 상품 조회 성공",
  "data": [
    {
      "productId": "P001",
      "name": "강아지 영양 간식 세트",
      "price": 18000,
      "totalSold": 153,
    },
    {
      "productId": "P002",
      "name": "고양이 모래 4kg",
      "price": 12000,
      "totalSold": 121,
    }
  ]
}
```

---

### ⚠️ Response (오류 예시)

#### 500 Internal Server Error

```json
{
  "code": 500,
  "message": "서버 내부 오류"
}
```

---

### 📘 응답 코드 설명

| 코드 | 설명                                      |
|------|-------------------------------------------|
| 200  | 인기 상품 목록 정상 반환                  |
| 500  | 서버 오류 또는 통계 집계 중 문제 발생       |

