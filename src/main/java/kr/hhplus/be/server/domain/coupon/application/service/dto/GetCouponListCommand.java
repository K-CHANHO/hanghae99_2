package kr.hhplus.be.server.domain.coupon.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetCouponListCommand {

    private String userId;

    public static GetCouponListCommand from(String userId) {
        return GetCouponListCommand.builder()
                .userId(userId)
                .build();
    }
}
