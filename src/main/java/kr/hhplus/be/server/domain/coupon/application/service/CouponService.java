package kr.hhplus.be.server.domain.coupon.application.service;

import kr.hhplus.be.server.common.aop.DistributedLock;
import kr.hhplus.be.server.domain.coupon.application.event.CouponUsedEvent;
import kr.hhplus.be.server.domain.coupon.application.service.dto.*;
import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.CouponStock;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.CouponStockRepository;
import kr.hhplus.be.server.domain.coupon.domain.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.order.application.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponStockRepository couponStockRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher publisher;

    @DistributedLock(prefix = "coupon:issue:", keys = "#couponCommand.couponId")
    @Transactional
    public IssueCouponResult issueCoupon(IssueCouponCommand couponCommand) {

        // 쿠폰 조회
        Coupon coupon = couponRepository.findById( couponCommand.getCouponId()).orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));

        // 발급받은 쿠폰인 지 확인
        if(userCouponRepository.findByUserIdAndCouponId(couponCommand.getUserId(), couponCommand.getCouponId()).isPresent()){
            throw new RuntimeException("이미 발급된 쿠폰입니다.");
        }

        // 쿠폰 잔여수량 차감
        CouponStock couponStock = couponStockRepository.findById(couponCommand.getCouponId())
                .orElseThrow(() -> new RuntimeException("쿠폰 잔여수량을 확인할 수 없습니다."));
        couponStock.reduce();
        couponStockRepository.save(couponStock);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(couponCommand.getUserId(), coupon);

        UserCoupon savedCoupon = userCouponRepository.save(userCoupon);
        return new IssueCouponResult(savedCoupon, coupon);
    }

    @DistributedLock(prefix = "coupon:issue:", keys = "#couponCommand.couponId")
    public void issueCouponRedis(IssueCouponCommand couponCommand){
        String userKey = "coupon:issue:" + couponCommand.getCouponId() + ":" + "userSet"; // 중복 확인 키
        String stockKey = "coupon:stock:" + couponCommand.getCouponId(); // 쿠폰 재고 키

        // 1. 중복 여부 확인
        Long added = redisTemplate.opsForSet().add(userKey, couponCommand.getUserId());
        if(added == null || added == 0L) {
            throw new RuntimeException("이미 발급된 쿠폰입니다.");
        }

        // 2. 쿠폰 재고 차감
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        log.info("coupon stock : {}", stock);
        if(stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(stockKey);
            redisTemplate.opsForSet().remove(userKey, couponCommand.getUserId());
            throw new RuntimeException("쿠폰이 소진되었습니다.");
        }

        // 3. DB 저장 비동기 처리
        saveCouponAsync(couponCommand);
    }

    @Async
    public void saveCouponAsync(IssueCouponCommand couponCommand) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponCommand.getCouponId())
                .orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.issue(couponCommand.getUserId(), coupon);

        userCouponRepository.save(userCoupon);
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

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event){
        UseCouponCommand useCouponCommand = UseCouponCommand.from(event);
        UseCouponResult useCouponResult = useCoupon(useCouponCommand);
        publisher.publishEvent(new CouponUsedEvent(event, useCouponResult));
    }

    public GetCouponListResult getCouponList(GetCouponListCommand getCouponListCommand) {
        List<UserCoupon> userCouponList = userCouponRepository.findByUserId(getCouponListCommand.getUserId());

        return GetCouponListResult.from(userCouponList);
    }
}
