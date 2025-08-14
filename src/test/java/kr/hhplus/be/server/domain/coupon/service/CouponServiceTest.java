package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.*;
import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
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

    @Mock
    private CouponStockRepository couponStockRepository;

    @ParameterizedTest
    @ValueSource(ints = {100, 101, 999})
    @DisplayName("쿠폰 발급 테스트_쿠폰 소진")
    public void issueCouponWithSoldOut(int issuedQuantity){
        // given
        Long couponId = 1L;
        String userId = "sampleUserId";
        Optional<Coupon> coupon = Optional.of(new Coupon(couponId, "샘플쿠폰", 100, "END", 0.1));
        Optional<CouponStock> couponStock = Optional.of(new CouponStock(couponId, 0L));
        IssueCouponCommand couponCommand = IssueCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        lenient().when(couponStockRepository.findById(couponId)).thenReturn(couponStock);
        lenient().when(couponStockRepository.findByIdWithPessimisticLock(couponId)).thenReturn(couponStock);

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

        lenient().when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(new UserCoupon()));

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
        UserCoupon userCoupon = UserCoupon.builder()
                .userCouponId(1L)
                .couponId(couponId)
                .userId("sampleUserId")
                .status("UNUSED")
                .issuedAt(new Timestamp(System.currentTimeMillis()))
                .expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)))
                .build();
        CouponStock couponStock = CouponStock.builder()
                .couponId(couponId).quantity(100L).build();
        IssueCouponCommand couponCommand = IssueCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(couponRepository.findById(couponId)).thenReturn(coupon);
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(userCoupon);
        lenient().when(couponStockRepository.findById(couponId)).thenReturn(Optional.of(couponStock));
        lenient().when(couponStockRepository.findByIdWithPessimisticLock(couponId)).thenReturn(Optional.of(couponStock));

        // when
        IssueCouponResult couponResult = couponService.issueCoupon(couponCommand);

        // then
        assertThat(couponResult.getCouponId()).isEqualTo(coupon.get().getCouponId());

    }

    @Test
    @DisplayName("쿠폰 사용 테스트_이미 사용한 쿠폰")
    public void useCouponWithUsedCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        UserCoupon userCoupon = UserCoupon.builder()
                .userCouponId(1L)
                .userId("sampleUserId")
                .status("USED")
                .issuedAt(new Timestamp(System.currentTimeMillis()))
                .expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS)))
                .build();
        UseCouponCommand useCouponCommand = UseCouponCommand.builder().userId(userId).couponId(couponId).build();

        when(userCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(userCoupon));

        // when, then
        assertThatThrownBy(() -> couponService.useCoupon(useCouponCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 사용한 쿠폰입니다.");

    }

    @Test
    @DisplayName("쿠폰 사용 테스트_만료된 쿠폰")
    public void useCouponWithExpiredCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        UserCoupon userCoupon = UserCoupon.builder()
                .userCouponId(1L)
                .userId(userId)
                .status("UNUSED")
                .issuedAt(new Timestamp(System.currentTimeMillis()))
                .expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().minus(1, ChronoUnit.DAYS)))
                .build();
        UseCouponCommand useCouponCommand = UseCouponCommand.builder().userId(userId).couponId(couponId).build();
        when(userCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(userCoupon));

        // when, then
        assertThatThrownBy(()-> couponService.useCoupon(useCouponCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("만료된 쿠폰입니다.");

    }

    @Test
    @DisplayName("쿠폰 사용 테스트")
    public void useCoupon(){
        // given
        String userId = "sampleUserId";
        Long couponId = 1L;
        Coupon coupon = Coupon.builder().couponId(couponId).build();
        UserCoupon unusedCoupon = UserCoupon.builder().couponId(couponId).status("UNUSED").expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plus(7, ChronoUnit.DAYS))).build();
        UserCoupon usedCoupon = UserCoupon.builder().couponId(couponId).status("USED").build();
        UseCouponCommand useCouponCommand = UseCouponCommand.builder().userId(userId).couponId(couponId).build();
        when(userCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(unusedCoupon));
        when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(usedCoupon);
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));

        // when
        UseCouponResult useCouponResult = couponService.useCoupon(useCouponCommand);

        // then
        assertThat(useCouponResult.getCouponId()).isEqualTo(couponId);
        assertThat(useCouponResult.getStatus()).isEqualTo("USED");
    }

    @Test
    @DisplayName("쿠폰 확인 테스트")
    public void getCouponList(){
        // given
        String userId = "sampleUserId";
        List<UserCoupon> sampleUserCoupons = List.of(
                UserCoupon.builder().userCouponId(1L).userId(userId).status("UNUSED").issuedAt(new Timestamp(System.currentTimeMillis())).expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().minus(1, ChronoUnit.DAYS))).couponId(1L).build(),
                UserCoupon.builder().userCouponId(2L).userId(userId).status("UNUSED").issuedAt(new Timestamp(System.currentTimeMillis())).expiredAt(Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().minus(1, ChronoUnit.DAYS))).couponId(2L).build()
        );

        GetCouponListCommand getCouponListCommand = GetCouponListCommand.from(userId);
        when(userCouponRepository.findByUserId(userId)).thenReturn(sampleUserCoupons);

        // when
        GetCouponListResult getCouponListResult = couponService.getCouponList(getCouponListCommand);

        // then
        assertThat(getCouponListResult).isNotNull();
        assertThat(getCouponListResult.getCouponDtoList().size()).isEqualTo(2);
    }


}
