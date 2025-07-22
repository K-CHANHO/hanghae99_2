package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.entity.Coupon;
import kr.hhplus.be.server.coupon.entity.UserCoupon;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.coupon.repository.UserCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @ParameterizedTest
    @ValueSource(ints = {100, 101, 999})
    @DisplayName("쿠폰 발급 테스트_쿠폰 소진")
    public void issueCouponWithSoldOut(int issuedQuantity){
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        Optional<Coupon> coupon = Optional.of(new Coupon(couponId, "샘플쿠폰", 100, "END", 0.1));

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.countByCouponId(couponId)).thenReturn(issuedQuantity);

        // when, then
        assertThatThrownBy(() -> couponService.issue(userId, couponId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("쿠폰이 소진되었습니다.");
    }

    @Test
    @DisplayName("쿠폰 발급 테스트")
    public void issueCoupon(){
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        Optional<Coupon> coupon = Optional.of(new Coupon(couponId, "샘플쿠폰", 100, "ING", 0.1));
        UserCoupon userCoupon = new UserCoupon(1L, "sampleUserId", coupon.get(), "UNUSED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)));
        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.countByCouponId(couponId)).thenReturn(10);
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(userCoupon);

        // when
        UserCoupon issuedCoupon = couponService.issue(userId, couponId);

        // then
        assertThat(issuedCoupon.getCoupon()).isEqualTo(coupon.get());

    }
}
