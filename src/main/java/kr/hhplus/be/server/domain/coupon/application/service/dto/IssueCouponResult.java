package kr.hhplus.be.server.domain.coupon.application.service.dto;

import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class IssueCouponResult {
    private Long userCouponId;
    private String userId;
    private String status; // 쿠폰 상태: AVAILABLE, USED, EXPIRED
    private Timestamp issuedAt;
    private Long couponId;
    private String couponName;

    public IssueCouponResult(UserCoupon savedCoupon, Coupon coupon) {
        this.userCouponId = savedCoupon.getUserCouponId();
        this.userId = savedCoupon.getUserId();
        this.status = savedCoupon.getStatus();
        this.issuedAt = savedCoupon.getIssuedAt();
        this.couponId = savedCoupon.getCouponId();
        this.couponName = coupon.getCouponName();
    }
}
