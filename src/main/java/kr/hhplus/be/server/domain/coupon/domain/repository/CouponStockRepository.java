package kr.hhplus.be.server.domain.coupon.domain.repository;

import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;

import java.util.Optional;

public interface CouponStockRepository {

    Optional<CouponStock> findById(Long couponId);

    Optional<CouponStock> findByIdWithPessimisticLock(Long couponId);


    CouponStock save(CouponStock couponStock);
}
