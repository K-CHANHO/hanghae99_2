package kr.hhplus.be.server.domain.coupon.domain.entity;

import jakarta.persistence.Entity;
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
public class CouponStock {

    @Id
    private Long couponId;
    private Long quantity; // 쿠폰 잔여 수량

    public void reduce() {
        if(quantity <= 0) throw new RuntimeException("쿠폰이 소진되었습니다.");
        quantity -= 1;
    }
}
