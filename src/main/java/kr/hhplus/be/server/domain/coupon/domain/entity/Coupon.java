package kr.hhplus.be.server.domain.coupon.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;
    private String couponName;
    private int quantity;
    private String status; // "ACTIVE", "INACTIVE"
    private Double discountRate;

    public void checkQuantity(int issuedCouponQuantity) {
        if(this.quantity <= issuedCouponQuantity) throw new RuntimeException("쿠폰이 소진되었습니다.");
    }
}
