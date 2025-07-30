package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.balance.application.service.dto.UseBalanceCommand;
import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.UseCouponCommand;
import kr.hhplus.be.server.domain.coupon.application.service.dto.UseCouponResult;
import kr.hhplus.be.server.domain.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.order.application.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.application.service.OrderProductService;
import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.*;
import kr.hhplus.be.server.domain.order.domain.entity.Order;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.payment.application.service.PaymentService;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayCommand;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayResult;
import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

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
    @Mock
    private ProductService productService;
    
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
                .status("PAID")
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
                .couponId(mockCoupon.getCouponId())
                .build();
        Payment mockPayment = Payment.builder()
                .paymentId(1L)
                .userId(userId)
                .orderId(mockOrder.getOrderId())
                .paidPrice((int) (mockOrder.getTotalPrice() * (1 - mockCoupon.getDiscountRate())))
                .status("PENDING")
                .build();

        PayResult payResult = new PayResult(mockPayment);

        OrderProcessCommand orderProcessCommand = OrderProcessCommand.builder()
                .userId(userId)
                .userCouponId(couponId)
                .orderProductDtoList(orderProductDtoList)
                .build();

        CreateOrderResult createOrderResult = CreateOrderResult.builder()
                .orderId(1L)
                .userId(userId)
                .totalPrice((int) orderProductDtoList.stream().mapToLong(dto -> (long) dto.getPrice() * dto.getQuantity()).sum())
                .status("PENDING")
                .build();

        ChangeStatusResult changeStatusResult = ChangeStatusResult.builder()
                .orderId(1L)
                .status("PAID")
                .build();

        OrderProductSaveResult.OrderProductDto2 d1 = OrderProductSaveResult.OrderProductDto2.builder()
                .orderProductId(1L)
                .price(10000)
                .orderId(1L)
                .build();
        List<OrderProductSaveResult.OrderProductDto2> dtos = new ArrayList<>();
        dtos.add(d1);
        OrderProductSaveResult orderProductSaveResult = OrderProductSaveResult.builder()
                .orderProductDto2List(dtos)
                .build();
        UseCouponResult useCouponResult = UseCouponResult.from(mockUserCoupon, mockCoupon);
        when(orderService.createOrder(any(CreateOrderCommand.class))).thenReturn(createOrderResult);
        when(orderService.changeStatus(any(ChangeStatusCommand.class))).thenReturn(changeStatusResult);
        when(couponService.useCoupon(any(UseCouponCommand.class))).thenReturn(useCouponResult);
        when(paymentService.pay(any(PayCommand.class))).thenReturn(payResult);
        when(orderProductService.save(any(OrderProductSaveCommand.class))).thenReturn(orderProductSaveResult);

        // when
        orderFacade.orderProcess(orderProcessCommand);

        // then
        verify(orderService).createOrder(any(CreateOrderCommand.class));
        verify(orderProductService).save(any(OrderProductSaveCommand.class));
        verify(couponService).useCoupon(any(UseCouponCommand.class));
        verify(paymentService).pay(any(PayCommand.class));
        verify(balanceService).useBalance(any(UseBalanceCommand.class));
        verify(orderService).changeStatus(any(ChangeStatusCommand.class));
    }
}
