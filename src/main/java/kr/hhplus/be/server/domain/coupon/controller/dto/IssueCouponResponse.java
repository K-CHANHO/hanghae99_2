package kr.hhplus.be.server.domain.coupon.controller.dto;

import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.dto.IssueCouponResult;
import lombok.Data;

@Data
public class IssueCouponResponse {
    private Long userCouponId;
    private Long couponId;
    private String couponName;

    public IssueCouponResponse(IssueCouponResult couponResult) {
        this.userCouponId = couponResult.getUserCouponId();
        this.couponId = couponResult.getCoupon().getCouponId();
        this.couponName = couponResult.getCoupon().getCouponName();
    }
}
