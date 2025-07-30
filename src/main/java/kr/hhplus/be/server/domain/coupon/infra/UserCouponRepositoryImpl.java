package kr.hhplus.be.server.domain.coupon.infra;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository jpaRepository;

    @Override
    public int countByCoupon_CouponId(Long couponId) {
        return jpaRepository.countByCoupon_CouponId(couponId);
    }

    @Override
    public Optional<UserCoupon> findByUserIdAndCoupon_CouponId(String userId, Long couponId) {
        return jpaRepository.findByUserIdAndCoupon_CouponId(userId, couponId);
    }

    @Override
    public List<UserCoupon> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaRepository.save(userCoupon);
    }
}
