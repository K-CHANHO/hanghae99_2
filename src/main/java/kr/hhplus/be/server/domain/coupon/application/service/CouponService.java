package kr.hhplus.be.server.domain.coupon.application.service;

import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public IssueCouponResult issueCoupon(IssueCouponCommand couponCommand) {

        Coupon coupon = couponRepository.findById( couponCommand.getCouponId()).orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));

        if(userCouponRepository.findByUserIdAndCoupon_CouponId(couponCommand.getUserId(), couponCommand.getCouponId()).isPresent()){
            throw new RuntimeException("이미 발급된 쿠폰입니다.");
        }

        int issuedCouponQuantity = userCouponRepository.countByCoupon_CouponId(couponCommand.getCouponId());
        coupon.checkQuantity(issuedCouponQuantity);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(couponCommand.getUserId(), coupon);

        UserCoupon savedCoupon = userCouponRepository.save(userCoupon);
        return new IssueCouponResult(savedCoupon);
    }

    public UserCoupon useCoupon(String userId, Long couponId) {
        if(couponId == null) return new UserCoupon();

        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCoupon_CouponId(userId, couponId).orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다"));
        userCoupon.useCoupon();
        return userCouponRepository.save(userCoupon);

    }

    public List<UserCoupon> viewCouponList(String userId) {
        return userCouponRepository.findByUserId(userId).orElse(null);
    }
}
