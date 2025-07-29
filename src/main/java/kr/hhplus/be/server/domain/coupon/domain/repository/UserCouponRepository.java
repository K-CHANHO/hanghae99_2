package kr.hhplus.be.server.domain.coupon.domain.repository;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    int countByCoupon_CouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCoupon_CouponId(String userId, Long couponId);

    Optional<List<UserCoupon>> findByUserId(String userId);

    UserCoupon save(UserCoupon userCoupon);

}
