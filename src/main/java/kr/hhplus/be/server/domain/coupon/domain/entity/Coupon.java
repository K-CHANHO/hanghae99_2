package kr.hhplus.be.server.domain.coupon.domain.entity;

import jakarta.persistence.*;
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
    @Column(length = 100)
    private String couponName;
    private int quantity; // 총 발급 수량
    @Column(length = 10)
    private String status; // "ACTIVE", "INACTIVE"
    private Double discountRate;

    public void checkQuantity(int issuedCouponQuantity) {
        if(this.quantity <= issuedCouponQuantity) throw new RuntimeException("쿠폰이 소진되었습니다.");
    }
}
