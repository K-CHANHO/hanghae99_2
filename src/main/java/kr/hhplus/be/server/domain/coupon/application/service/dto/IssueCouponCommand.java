package kr.hhplus.be.server.domain.coupon.application.service.dto;

import kr.hhplus.be.server.domain.coupon.presenter.controller.dto.IssueCouponRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueCouponCommand {

    private String userId; // 사용자 ID
    private Long couponId; // 쿠폰 ID

    public IssueCouponCommand(IssueCouponRequest request) {
        this.userId = request.getUserId();
        this.couponId = request.getCouponId();
    }
}
