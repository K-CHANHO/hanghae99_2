package kr.hhplus.be.server.domain.coupon.application.service.dto;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
public class ViewCouponListResult {
    private List<CouponDto> couponDtoList;

    public static ViewCouponListResult from(List<UserCoupon> userCouponList) {
        List<CouponDto> list = userCouponList.stream().map(CouponDto::from).toList();
        return ViewCouponListResult.builder()
                .couponDtoList(list)
                .build();

    }

    @Builder
    @Getter
    @AllArgsConstructor
    static class CouponDto {
        private Long userCouponId;
        private String userId;
        private String status; // 쿠폰 상태: AVAILABLE, USED, EXPIRED
        private Timestamp issuedAt;
        private Timestamp expiredAt;
        private Timestamp usedAt;

        public static CouponDto from(UserCoupon userCoupon){
            return CouponDto.builder()
                    .userCouponId(userCoupon.getUserCouponId())
                    .userId(userCoupon.getUserId())
                    .status(userCoupon.getStatus())
                    .issuedAt(userCoupon.getIssuedAt())
                    .expiredAt(userCoupon.getExpiredAt())
                    .usedAt(userCoupon.getUsedAt())
                    .build();
        }
    }

}
