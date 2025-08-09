package kr.hhplus.be.server.domain.coupon.application.service;

import kr.hhplus.be.server.domain.coupon.application.service.dto.*;
import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponStockRepository couponStockRepository;

    @Transactional
    public IssueCouponResult issueCoupon(IssueCouponCommand couponCommand) {

        // 쿠폰 조회
        Coupon coupon = couponRepository.findById( couponCommand.getCouponId()).orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));

        // 발급받은 쿠폰인 지 확인
        if(userCouponRepository.findByUserIdAndCouponId(couponCommand.getUserId(), couponCommand.getCouponId()).isPresent()){
            throw new RuntimeException("이미 발급된 쿠폰입니다.");
        }

        // 발급된 쿠폰 수 확인
        //int issuedCouponQuantity = userCouponRepository.countByCouponId(couponCommand.getCouponId());
        //coupon.checkQuantity(issuedCouponQuantity);
        // 쿠폰 잔여수량 차감
        CouponStock couponStock = couponStockRepository.findByIdWithPessimisticLock(couponCommand.getCouponId())
                .orElseThrow(() -> new RuntimeException("쿠폰 잔여수량을 확인할 수 없습니다."));
        couponStock.reduce();
        couponStockRepository.save(couponStock);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(couponCommand.getUserId(), coupon);

        UserCoupon savedCoupon = userCouponRepository.save(userCoupon);
        return new IssueCouponResult(savedCoupon, coupon);
    }

    public UseCouponResult useCoupon(UseCouponCommand useCouponCommand) {
        if(useCouponCommand.getCouponId() == null) {
            return UseCouponResult.builder().discountRate(0.0).build();
        }

        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(useCouponCommand.getUserId(), useCouponCommand.getCouponId())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다"));
        userCoupon.useCoupon();
        UserCoupon usedCoupon = userCouponRepository.save(userCoupon);

        Coupon coupon = couponRepository.findById(useCouponCommand.getCouponId())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 쿠폰입니다."));
        return UseCouponResult.from(usedCoupon, coupon);

    }

    public ViewCouponListResult viewCouponList(ViewCouponListCommand viewCouponListCommand) {
        List<UserCoupon> userCouponList = userCouponRepository.findByUserId(viewCouponListCommand.getUserId());

        return ViewCouponListResult.from(userCouponList);
    }
}
