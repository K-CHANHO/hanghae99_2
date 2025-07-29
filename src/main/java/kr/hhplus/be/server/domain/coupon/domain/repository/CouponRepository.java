package kr.hhplus.be.server.domain.coupon.domain.repository;

import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long couponId);
}
