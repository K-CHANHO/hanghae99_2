package kr.hhplus.be.server.domain.coupon.infra;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponStockJpaRepository extends JpaRepository<CouponStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cs FROM CouponStock cs WHERE cs.couponId = :couponId")
    Optional<CouponStock> findByIdWithPessimisticLock(@Param("couponId") Long couponId);
}
