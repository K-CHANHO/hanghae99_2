package kr.hhplus.be.server.domain.coupon.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ViewCouponListCommand {

    private String userId;

    public static ViewCouponListCommand from(String userId) {
        return ViewCouponListCommand.builder()
                .userId(userId)
                .build();
    }
}
