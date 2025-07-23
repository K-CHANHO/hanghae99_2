package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.balance.entity.Balance;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import kr.hhplus.be.server.balance.service.BalanceService;
import kr.hhplus.be.server.coupon.entity.UserCoupon;
import kr.hhplus.be.server.coupon.service.CouponService;
import kr.hhplus.be.server.order.entity.Order;
import kr.hhplus.be.server.order.entity.OrderProduct;
import kr.hhplus.be.server.order.service.OrderService;
import kr.hhplus.be.server.payment.entity.Payment;
import kr.hhplus.be.server.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class OrderFacadeTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceRepository balanceRepository;
    @BeforeEach
    public void initBalance(){
        Balance balance = Balance.builder().userId("sampleUserId").balance(1000000).build();
        balanceRepository.save(balance);
    }
    @Test
    public void orderProcess(){
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProduct> productsList = new ArrayList<>();
        Long couponId = 1L;

        // when
        // 주문 생성
        Order order = orderService.createOrder(userId, productsList);

        // 주문 상품 저장


        // 쿠폰 적용
        UserCoupon userCoupon = couponService.useCoupon(userId, couponId);
        double discountRate = 0.0;
        if(userCoupon != null){
            discountRate = userCoupon.getCoupon().getDiscountRate();
        }

        // 결제 생성
        Payment payment = paymentService.create(userId, order.getOrderId(), order.getTotalPrice(), discountRate);

        // 결제 요청
        paymentService.pay(userId, payment.getPaymentId());

        // 주문 상태 변경
        orderService.changeStatus(order.getOrderId(), "PAID");

        // 잔액 차감
        balanceService.useBalance(userId, payment.getPaidPrice());

        // 외부 API 호출
        paymentService.sendPaymentNotification(payment);
    }
}
