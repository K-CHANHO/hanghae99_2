package kr.hhplus.be.server.domain.coupon.dto;

import lombok.Data;

@Data
public class IssueCouponResponse {
    private String couponId;
    private String ruleId;
    private String couponName;
    private String status;
    private int totalQuantity;
    private int remainQuantity;
}
