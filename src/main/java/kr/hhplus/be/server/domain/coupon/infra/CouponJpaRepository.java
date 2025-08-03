package kr.hhplus.be.server.domain.coupon.infra;

import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
