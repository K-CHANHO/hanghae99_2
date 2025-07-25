package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    int countByCouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(String userId, Long couponId);

    Optional<List<UserCoupon>> findByUserId(String userId);

}
