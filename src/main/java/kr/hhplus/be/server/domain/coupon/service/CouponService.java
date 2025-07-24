package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public UserCoupon issueCoupon(String userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));
        if(userCouponRepository.findByUserIdAndCouponId(userId, couponId).isPresent()) throw new RuntimeException("이미 발급된 쿠폰입니다.");
        int issuedCouponQuantity = userCouponRepository.countByCouponId(couponId);
        coupon.checkQuantity(issuedCouponQuantity);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(userId, coupon);

        return userCouponRepository.save(userCoupon);
    }

    public UserCoupon useCoupon(String userId, Long couponId) {
        try{
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId).orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다"));
            userCoupon.useCoupon();
            return userCouponRepository.save(userCoupon);
        }
        catch (RuntimeException e){
            return null;
        }
    }

    public List<UserCoupon> viewCouponList(String userId) {
        return userCouponRepository.findByUserId(userId).orElse(null);
    }
}
