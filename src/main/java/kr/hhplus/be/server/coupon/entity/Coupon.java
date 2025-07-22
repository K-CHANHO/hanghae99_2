package kr.hhplus.be.server.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class Coupon {
    @Id
    private Long couponId;
    private String couponName;
    private int quantity;
    private String status;
    private double discountRate;

    public void checkQuantity(int issuedCouponQuantity) {
        if(this.quantity <= issuedCouponQuantity) throw new RuntimeException("쿠폰이 소진되었습니다.");
    }
}
