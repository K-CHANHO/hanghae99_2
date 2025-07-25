package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

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
    private Long couponId;
    private String status; // 쿠폰 상태: AVAILABLE, USED, EXPIRED
    private Timestamp issuedAt;
    private Timestamp expiredAt;
    private Timestamp usedAt;

    @Transient @Setter
    private Coupon coupon;

    public void issue(String userId, Coupon coupon) {
        this.userId = userId;
        this.couponId = coupon.getCouponId();
        this.coupon = coupon;
        this.issuedAt = new Timestamp(System.currentTimeMillis());
        this.expiredAt = Timestamp.from(issuedAt.toInstant().plus(7, ChronoUnit.DAYS));
    }

    public void useCoupon() {
        if(this.status.equals("USED")) throw new RuntimeException("이미 사용한 쿠폰입니다.");
        else if(this.expiredAt.before(new Timestamp(System.currentTimeMillis()))) throw new RuntimeException("만료된 쿠폰입니다.");
        this.status = "USED";
        this.usedAt = new Timestamp(System.currentTimeMillis());
    }
}
