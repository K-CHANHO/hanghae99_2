package kr.hhplus.be.server.domain.coupon.presenter.controller.dto;

import lombok.Data;

@Data
public class IssueCouponRequest {
    private String userId; // 사용자 ID
    private Long couponId; // 쿠폰 ID
    private String transactionId; // 거래 ID (중복 방지용)
}
