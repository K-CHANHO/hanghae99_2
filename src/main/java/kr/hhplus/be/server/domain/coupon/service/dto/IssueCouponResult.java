package kr.hhplus.be.server.domain.coupon.service.dto;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
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
    private Coupon coupon;

    public IssueCouponResult(UserCoupon savedCoupon) {
        this.userCouponId = savedCoupon.getUserCouponId();
        this.userId = savedCoupon.getUserId();
        this.status = savedCoupon.getStatus();
        this.issuedAt = savedCoupon.getIssuedAt();
        this.coupon = savedCoupon.getCoupon();
    }
}
