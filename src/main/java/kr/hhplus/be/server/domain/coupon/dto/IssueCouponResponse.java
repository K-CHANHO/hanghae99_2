package kr.hhplus.be.server.domain.coupon.dto;

import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import lombok.Data;

@Data
public class IssueCouponResponse {
    private Long userCouponId;
    private Long couponId;
    private String couponName;

    public void from(UserCoupon userCoupon) {
        this.userCouponId = userCoupon.getUserCouponId();
        this.couponId = userCoupon.getCoupon().getCouponId();
        this.couponName = userCoupon.getCoupon().getCouponName();
    }
}
