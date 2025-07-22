package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    int countByCouponId(Long couponId);
}
