package kr.hhplus.be.server.coupon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Timestamp usedAt;

    public void issue(String userId, Coupon coupon) {
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = new Timestamp(System.currentTimeMillis());
        this.expiredAt = Timestamp.from(issuedAt.toInstant().plus(7, ChronoUnit.DAYS));
    }

    public void useCoupon() {
        if(this.status.equals("USED")) throw new RuntimeException("이미 사용한 쿠폰입니다.");
        this.status = "USED";
        this.usedAt = new Timestamp(System.currentTimeMillis());
    }
}
