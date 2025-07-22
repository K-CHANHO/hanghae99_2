package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.entity.Coupon;
import kr.hhplus.be.server.coupon.entity.UserCoupon;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public UserCoupon issue(String userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));
        int issuedCouponQuantity = userCouponRepository.countByCouponId(couponId);
        coupon.checkQuantity(issuedCouponQuantity);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(userId, coupon);

        return userCouponRepository.save(userCoupon);
    }
}
