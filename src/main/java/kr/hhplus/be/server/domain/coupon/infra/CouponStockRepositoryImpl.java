package kr.hhplus.be.server.domain.coupon.infra;

import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponStockRepositoryImpl implements CouponStockRepository {

    private final CouponStockJpaRepository jpaRepository;

    @Override
    public Optional<CouponStock> findById(Long couponId) {
        return jpaRepository.findByIdWithPessimisticLock(couponId);
    }

    @Override
    public CouponStock save(CouponStock couponStock) {
        return jpaRepository.save(couponStock);
    }
}
