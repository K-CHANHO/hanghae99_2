package kr.hhplus.be.server.domain.coupon.domain.repository;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    int countByCouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(String userId, Long couponId);

    List<UserCoupon> findByUserId(String userId);

    UserCoupon save(UserCoupon userCoupon);

}
