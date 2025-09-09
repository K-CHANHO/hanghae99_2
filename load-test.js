import http from 'k6/http';
import { check, sleep } from 'k6';

// ====== 시나리오별 설정 ======
export let options = {
  scenarios: {
    // 1. 정상 발급 시나리오
    normal_issue: {
      executor: 'constant-vus',
      vus: 10,                 // 10명의 유저가 동시에 요청
      duration: '30s',
      exec: 'normalIssue',
    },
    // 2. 대량 발급 시나리오
    bulk_issue: {
      executor: 'constant-vus',
      vus: 1000,               // 1,000명 동시 요청
      duration: '30s',
      exec: 'bulkIssue',
    },
    // 3. 부하 한계 시나리오
    load_limit: {
      executor: 'ramping-vus',
      startVUs: 10,
      stages: [
        { duration: '30s', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '30s', target: 200 },
        { duration: '30s', target: 500 },
        { duration: '30s', target: 0 },
      ],
      exec: 'loadTest',
    },
    // 4. 내구성(Soak) 시나리오
    /*
    soak_test: {
      executor: 'constant-vus',
      vus: 50,
      duration: '2h',
      exec: 'soakTest',
    },*/
  },
};

// ====== 공통 요청 함수 ======
function sendCouponRequest(userId, couponId, transactionId) {
  const url = 'http://localhost:8080/api/v1/coupon/issue';
  const payload = JSON.stringify({
    userId: userId,
    couponId: couponId,
    transactionId: transactionId,
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  let res = http.post(url, payload, params);
  check(res, { 'status is 200': (r) => r.status === 200 });
  sleep(1); // think time
}

// ====== 시나리오별 실행 함수 ======
export function normalIssue() {
  sendCouponRequest(`user-${__VU}-${__ITER}`, 1, `tx-${__VU}-${__ITER}`);
}

export function bulkIssue() {
  // 동일 쿠폰 ID에 대해 많은 사용자 요청
  sendCouponRequest(`user-${__VU}-${__ITER}`, 1, `tx-${__VU}-${__ITER}`);
}

export function loadTest() {
  sendCouponRequest(`user-${__VU}-${__ITER}`, 1, `tx-${__VU}-${__ITER}`);
}

export function soakTest() {
  sendCouponRequest(`user-${__VU}-${__ITER}`, 1, `tx-${__VU}-${__ITER}`);
}
