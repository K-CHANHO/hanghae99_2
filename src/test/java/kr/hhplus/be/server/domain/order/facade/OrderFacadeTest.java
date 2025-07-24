package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.balance.service.BalanceService;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderFacadeTest {
    @InjectMocks
    private OrderFacade orderFacade;
    @Mock
    private OrderService orderService;
    @Mock
    private CouponService couponService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private BalanceService balanceService;
    @Mock
    private OrderProductService orderProductService;
    
    @Test
    public void orderProcess(){
        // given
        String userId = "sampleUserId";
        List<OrderProductDto> orderProductDtoList = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> OrderProductDto.builder()
                        .productId((long) i)
                        .price(i * 10000)
                        .quantity(i)
                        .build())
                .collect(Collectors.toList());
        Long couponId = 1L;
        Order mockOrder = Order.builder()
                .orderId(1L)
                .userId(userId)
                .totalPrice((int) orderProductDtoList.stream().mapToLong(dto -> (long) dto.getPrice() * dto.getQuantity()).sum())
                .status("PENDING")
                .build();
        Coupon mockCoupon = Coupon.builder()
                .couponId(couponId)
                .discountRate(0.1)
                .couponName("샘플 쿠폰")
                .quantity(100)
                .status("ACTIVE")
                .build();
        UserCoupon mockUserCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .coupon(mockCoupon)
                .build();
        Payment mockPayment = Payment.builder()
                .paymentId(1L)
                .userId(userId)
                .orderId(mockOrder.getOrderId())
                .paidPrice((int) (mockOrder.getTotalPrice() * (1 - mockCoupon.getDiscountRate())))
                .status("PENDING")
                .build();
        Payment mockPaidPayment = Payment.builder()
                .paymentId(1L)
                .userId(userId)
                .orderId(mockOrder.getOrderId())
                .paidPrice((int) (mockOrder.getTotalPrice() * (1 - mockCoupon.getDiscountRate())))
                .status("PAID")
                .build();

        when(orderService.createOrder(userId, orderProductDtoList)).thenReturn(mockOrder);
        when(couponService.useCoupon(userId, couponId)).thenReturn(mockUserCoupon);
        when(paymentService.create(userId, mockOrder.getOrderId(), mockOrder.getTotalPrice(), mockCoupon.getDiscountRate())).thenReturn(mockPayment);
        when(paymentService.pay(mockPayment)).thenReturn(mockPaidPayment);

        // when
        orderFacade.orderProcess(userId, orderProductDtoList, couponId);

        // then
        verify(orderService).createOrder(userId, orderProductDtoList);
        verify(orderProductService).save(userId, mockOrder.getOrderId(), orderProductDtoList);
        verify(couponService).useCoupon(userId, couponId);
        verify(paymentService).create(userId, mockOrder.getOrderId(), mockOrder.getTotalPrice(), mockCoupon.getDiscountRate());
        verify(paymentService).pay(mockPayment);
        verify(balanceService).useBalance(userId, mockPaidPayment.getPaidPrice());
        verify(orderService).changeStatus(mockOrder.getOrderId(), "PAID");
        verify(paymentService).sendPaymentNotification(mockPayment);
    }
}
