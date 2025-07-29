package kr.hhplus.be.server.domain.coupon.infra;

import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return jpaRepository.findById(couponId);
    }
}
