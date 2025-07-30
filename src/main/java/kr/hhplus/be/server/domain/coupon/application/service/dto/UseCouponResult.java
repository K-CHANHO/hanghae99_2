package kr.hhplus.be.server.domain.coupon.application.service.dto;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UseCouponResult {
    private Long userCouponId;
    private Long couponId;
    private String userId;
    private String status; // 쿠폰 상태: AVAILABLE, USED, EXPIRED
    private Timestamp issuedAt;
    private Timestamp expiredAt;
    private Timestamp usedAt;
    private Double discountRate;

    public static UseCouponResult from(UserCoupon userCoupon) {
        return UseCouponResult.builder()
                .userCouponId(userCoupon.getUserCouponId())
                .couponId(userCoupon.getCoupon().getCouponId())
                .userId(userCoupon.getUserId())
                .status(userCoupon.getStatus())
                .issuedAt(userCoupon.getIssuedAt())
                .usedAt(userCoupon.getUsedAt())
                .discountRate(userCoupon.getCoupon().getDiscountRate())
                .build();
    }
}
