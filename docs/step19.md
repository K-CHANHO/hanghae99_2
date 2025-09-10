# 쿠폰 발급 서비스 부하 테스트 계획서

## 1. 테스트 목적
- 대규모 트래픽 상황에서 **쿠폰 발급 서비스의 안정성 및 성능** 검증
- Kafka를 통한 **비동기 메시징 처리의 한계 지점** 파악
- 병목 지점(애플리케이션, Kafka 브로커, Redis/DB 등) 식별
- 성능 최적화 및 인프라 스케일링 기준 마련

---

## 2. 테스트 대상
### 2.1 대상 서비스
- **쿠폰 발급 API**
    - Endpoint: `/api/v1/coupon/issue`
    - Method: `POST`
    - 요청 시 Kafka Producer가 `coupon-issue` 토픽에 메시지를 전송
    - Kafka Consumer가 메시지를 수신하여 실제 `couponService.issueCoupon` 호출

### 2.2 주요 처리 흐름
1. 클라이언트가 쿠폰 발급 API 호출
2. Kafka Producer → `coupon-issue` 토픽 메시지 발행
3. Kafka Consumer → 메시지 수신 및 쿠폰 발급 처리
4. DB 또는 Redis에 쿠폰 발급 내역 저장

---

## 3. 부하 테스트 시나리오
### 3.1 기본 시나리오
- 다수의 사용자가 동시에 쿠폰 발급 API를 호출
- 정상적으로 Kafka Producer/Consumer를 통해 쿠폰이 발급되는지 확인

### 3.2 상세 시나리오
1. **정상 발급 시나리오**
    - 요청: `{ userId, couponId, transactionId }`
    - 응답: `200 OK`
    - Kafka 메시지 발행 및 소비 정상 동작
2. **대량 발급 시나리오**
    - 단일 쿠폰 ID에 대해 대규모 사용자(동시 1,000명 이상) 요청
    - 중복 발급 여부, DB/Redis Lock 처리 안정성 확인
3. **부하 한계 시나리오**
    - VU(Virtual Users)를 점진적으로 증가시키며 Kafka 또는 DB에서 병목 발생 시점 확인
4. **내구성 시나리오(Soak Test)**
    - 장시간(예: 2시간) 동안 일정 수준의 트래픽 유지
    - 메모리 누수, Kafka Lag 발생 여부 모니터링

---

## 4. 테스트 방법
- **부하 생성 도구**: [k6](https://k6.io/)
- **실행 환경**
    - Docker 기반 Kafka, Redis, DB 구성
    - Spring Boot 애플리케이션 실행
- **테스트 전략**
    - 단계별 VU 증가 (예: 50 → 100 → 200 → 500)
    - 일정 기간(예: 1분) 동안 부하 유지 후 모니터링
    - 임계치 초과 시 성능 저하 원인 분석

---

## 5. 성능 지표
- **응답 지표**
    - 평균 응답 시간 (Average Response Time)
    - 95th/99th Percentile 응답 시간
    - 오류율 (HTTP 4xx/5xx 비율)
- **시스템 지표**
    - Kafka Consumer Lag
    - CPU, Memory 사용률
    - DB/Redis Connection Pool 사용률
- **비즈니스 지표**
    - 초당 쿠폰 발급 처리량 (TPS/QPS)
    - 중복 발급 여부

---

## 6. 성공 기준
- 95% 이상의 요청이 **500ms 이하 응답 시간** 보장
- 오류율 **1% 이하**
- Kafka Lag이 일정 수준 이하 유지
- 중복 쿠폰 발급 발생하지 않을 것

---

## 7. 결과 활용 계획
- 병목 구간(애플리케이션, Kafka, DB/Redis) 식별
- 인프라 스케일링 기준 설정 (예: Kafka 파티션 수, Consumer 그룹 수)
- 코드 최적화 포인트 도출 (예: 동시성 제어, 비동기 처리 개선)
- 운영 환경 SLA(Service Level Agreement) 수립 근거 마련
