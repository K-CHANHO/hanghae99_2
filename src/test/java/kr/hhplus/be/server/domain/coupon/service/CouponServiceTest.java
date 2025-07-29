package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponResult;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
        IssueCouponCommand couponCommand = IssueCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.countByCoupon_CouponId(couponId)).thenReturn(issuedQuantity);

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(couponCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("쿠폰이 소진되었습니다.");
    }

    @Test
    @DisplayName("쿠폰 발급 테스트_이미 발급된 쿠폰")
    public void issueCouponWithAlreadyIssued(){
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        Optional<Coupon> coupon = Optional.of(new Coupon(couponId, "샘플쿠폰", 100, "ING", 0.1));
        IssueCouponCommand couponCommand = IssueCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.findByUserIdAndCoupon_CouponId(userId, couponId)).thenReturn(Optional.of(new UserCoupon()));

        // when, then
        assertThatThrownBy(() -> couponService.issueCoupon(couponCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 발급된 쿠폰입니다.");
    }

    @Test
    @DisplayName("쿠폰 발급 테스트")
    public void issueCoupon(){
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        Optional<Coupon> coupon = Optional.of(new Coupon(couponId, "샘플쿠폰", 100, "ING", 0.1));
        UserCoupon userCoupon = new UserCoupon(1L, "sampleUserId", "UNUSED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)), null, coupon.get());
        IssueCouponCommand couponCommand = IssueCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.countByCoupon_CouponId(couponId)).thenReturn(10);
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(userCoupon);

        // when
        IssueCouponResult couponResult = couponService.issueCoupon(couponCommand);

        // then
        assertThat(couponResult.getCoupon().getCouponId()).isEqualTo(coupon.get().getCouponId());

    }

    @Test
    @DisplayName("쿠폰 사용 테스트_이미 사용한 쿠폰")
    public void useCouponWithUsedCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        UserCoupon userCoupon = new UserCoupon(1L, "sampleUserId", "USED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)), null, new Coupon());
        when(userCouponRepository.findByUserIdAndCoupon_CouponId(userId, couponId)).thenReturn(Optional.of(userCoupon));

        // when
        UserCoupon usedCoupon = couponService.useCoupon(userId, couponId);

        // then
        assertThat(usedCoupon).isNull();
        verify(userCouponRepository, never()).save(any(UserCoupon.class));
    }

    @Test
    @DisplayName("쿠폰 사용 테스트_만료된 쿠폰")
    public void useCouponWithExpiredCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        UserCoupon userCoupon = new UserCoupon(1L, userId, "UNUSED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().minus(1, ChronoUnit.DAYS)), null, new Coupon());
        when(userCouponRepository.findByUserIdAndCoupon_CouponId(userId, couponId)).thenReturn(Optional.of(userCoupon));

        // when
        UserCoupon expiredCoupon = couponService.useCoupon(userId, couponId);

        // then
        assertThat(expiredCoupon).isNull();
        verify(userCouponRepository, never()).save(any(UserCoupon.class));
    }

    @Test
    @DisplayName("쿠폰 사용 테스트")
    public void useCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        Coupon coupon = Coupon.builder().couponId(couponId).build();
        UserCoupon unusedCoupon = UserCoupon.builder().coupon(coupon).status("UNUSED").expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS))).build();
        UserCoupon usedCoupon = UserCoupon.builder().coupon(coupon).status("USED").build();

        when(userCouponRepository.findByUserIdAndCoupon_CouponId(userId, couponId)).thenReturn(Optional.of(unusedCoupon));
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(usedCoupon);

        // when
        UserCoupon userCoupon = couponService.useCoupon(userId, couponId);

        // then
        assertThat(userCoupon.getCoupon().getCouponId()).isEqualTo(couponId);
        assertThat(userCoupon.getStatus()).isEqualTo("USED");
    }

    @Test
    @DisplayName("쿠폰 확인 테스트")
    public void viewCouponList(){
        // given
        String userId = "sampleUserId";
        List<UserCoupon> sampleUserCoupons = List.of(
                new UserCoupon(1L, userId, "UNUSED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)), null, new Coupon(1L, "쿠폰1", 100, "ING", 0.1)),
                new UserCoupon(2L, userId, "UNUSED", new Timestamp(System.currentTimeMillis()), Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)), null, new Coupon(2L, "쿠폰2", 200, "ING", 0.2))
        );
        when(userCouponRepository.findByUserId(userId)).thenReturn(Optional.of(sampleUserCoupons));

        // when
        List<UserCoupon> userCouponList = couponService.viewCouponList(userId);

        // then
        assertThat(userCouponList).isNotNull();
        assertThat(userCouponList.size()).isEqualTo(2);
    }


}
