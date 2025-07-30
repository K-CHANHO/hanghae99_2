package kr.hhplus.be.server.domain.coupon.infra;

import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    int countByCouponId(Long couponId);

    Optional<UserCoupon> findByUserIdAndCouponId(String userId, Long couponId);

    List<UserCoupon> findByUserId(String userId);

}
