package kr.hhplus.be.server.coupon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCouponId;
    private String userId;
    @OneToOne
    @JoinColumn(name = "coupon_couponId")
    private Coupon coupon;
    private String status;
    private Timestamp issuedAt;
    private Timestamp expiredAt;

    public void issue(String userId, Coupon coupon) {
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = new Timestamp(System.currentTimeMillis());
        this.expiredAt = Timestamp.from(issuedAt.toInstant().plus(7, ChronoUnit.DAYS));
    }
}
