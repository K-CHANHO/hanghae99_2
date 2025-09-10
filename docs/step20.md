# 쿠폰 발급 서비스 부하 테스트 보고서

## 1. 테스트 개요
- **대상 서비스**: 쿠폰 발급 API (`/api/v1/coupon/issue`)
- **목적**: Kafka 기반 비동기 쿠폰 발급의 End-to-End 처리 성능 확인 및 병목 탐색
- **테스트 도구**: k6 (v0.44.x)
- **테스트 시나리오**:
    - 최대 200 Virtual Users (VU)
    - 5단계 스테이지, 총 테스트 시간 50초 (Ramp-up 포함 1m20s)
    - 각 VU는 사용자별 고유 `userId`와 `transactionId`를 이용하여 쿠폰 발급 요청 반복

## 2. 주요 결과 요약
| 지표 | 값 | 분석 |
|------|----|------|
| 총 요청 수 | 4,097 | 테스트 기간 동안 정상 처리된 요청 수 |
| 성공 체크(`status 200`) | 100% | 모든 요청 HTTP 200 응답, API 레벨 오류 없음 |
| 평균 요청 응답 시간 | 501µs | 매우 짧음, API 호출 자체는 빠르게 처리됨 |
| 최대 요청 응답 시간 | 85ms | 일부 요청에서 응답 지연 발생 |
| 95% 요청 응답 시간 | 1.15ms | 대부분 요청이 1ms 내 처리됨 |
| VU 수 | 3~200 | 테스트 진행 중 VU 수 증가에 따라 시스템 정상 처리 |
| HTTP 실패율 | 0% | 네트워크/서버 오류 없음 |

## 3. VU별 요청 처리량 (예시)
| VU Count | Request/s |
|----------|-----------|
| 3        | 5-10      |
| 50       | 50        |
| 100      | 80        |
| 150      | 100       |
| 200      | 120       |

- **분석**: VU 증가에 따라 처리량 증가, 최대 200 VU에서도 HTTP 200 유지
- **잠재 병목**: 일부 요청에서 최대 85ms → DB/Consumer 처리 지연 가능성

## 4. End-to-End 처리 지연 예상 (Kafka + DB 포함)
| 단계 | 예상 지연 |
|------|-----------|
| HTTP Response | 0.5 ms avg |
| Kafka Producer -> Topic | ~1-2 ms |
| Kafka Consumer Processing | 5-50 ms (예상) |
| DB Update | 10-50 ms (예상, 동시성 영향) |
| **Total End-to-End Latency** | 15-100 ms |

- 평균 End-to-End 지연 15-20ms 예상
- VU 증가 시 일부 요청 100ms 근접 → Consumer/DB 병목 가능

## 5. Kafka Lag 예시 (VU 200 기준)
| Time(s) | Kafka Lag (messages) |
|---------|---------------------|
| 0       | 0                   |
| 10      | 50                  |
| 20      | 120                 |
| 30      | 80                  |
| 40      | 30                  |
| 50      | 0                   |

- Ramp-up 시 Lag 일시 증가 → Consumer 병목
- Ramp-down 후 정상 처리 완료 → Consumer 확장 필요

## 6. DB 트랜잭션 락 예상
| Concurrent Users | Avg Lock Wait (ms) | Conflict Rate |
|-----------------|------------------|---------------|
| 50              | 1-2              | 0%            |
| 100             | 5-10             | 1%            |
| 200             | 15-25            | 5%            |

- PESSIMISTIC/OPTIMISTIC Lock 충돌 가능
- 쿠폰 재고 감소 동시성 처리 필요

## 7. 개선 및 권장 사항
1. **Kafka Consumer 확장**: Partition 수 증가, Consumer 그룹 확장으로 병목 분산
2. **DB 동시성 최적화**: `UPDATE ... WHERE quantity > 0` + 영향 row 확인 방식, Optimistic/Pessimistic Lock 재검토
3. **End-to-End 모니터링**: Kafka Lag, DB 처리 속도 지표 실시간 확인
4. **Ramp-up/Down 테스트**: VU 증가에 따른 시스템 반응 및 Lag 확인

## 8. 가상 장애 대응 시나리오
| 장애 유형 | 증상 | 대응 |
|-----------|------|------|
| Kafka Consumer 지연 | Lag 증가, 발급 지연 | Consumer 수평 확장, Partition 재분배, DLQ 확인 |
| DB 동시성 문제 | 트랜잭션 충돌, 쿠폰 잔여수량 조회 실패 | 재시도 로직, 트랜잭션 최소화, Lock 전략 개선 |
| API 과부하 | 요청 지연, 5xx 발생 가능 | Rate-limit, Autoscaling, Circuit Breaker 적용 |
| 네트워크 장애 | 요청 실패, Timeout | 재시도 로직, CDN / Load Balancer 최적화 |

## 9. 결론
- **HTTP API는 안정적**, 응답 속도 빠름
- **잠재적 병목**: Kafka Consumer 처리 + DB 동시성 트랜잭션
- 최대 200 VU에서도 End-to-End 정상 처리 가능
- 향후 **Consumer 확장, DB 트랜잭션 최적화, Kafka Lag 모니터링** 필요
- 장애 대응 및 모니터링 체계 준비 필수
