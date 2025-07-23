package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    int countByCoupon_CouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCoupon_CouponId(String userId, Long couponId);

    Optional<List<UserCoupon>> findByUserId(String userId);
}
