package kr.hhplus.be.server.domain.order.application.facade;

import kr.hhplus.be.server.domain.balance.application.service.BalanceService;
import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.domain.entity.UserCoupon;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessResult;
import kr.hhplus.be.server.domain.order.application.service.OrderProductService;
import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.*;
import kr.hhplus.be.server.domain.payment.application.service.PaymentService;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayCommand;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final BalanceService balanceService;
    private final ProductService productService;

    @Transactional
    public OrderProcessResult orderProcess(OrderProcessCommand orderProcessCommand) {

        // 주문 생성
        CreateOrderCommand createOrderCommand = CreateOrderCommand.from(orderProcessCommand);
        CreateOrderResult createOrderResult = orderService.createOrder(createOrderCommand);

        // 주문 상품 저장
        OrderProductSaveCommand orderProductSaveCommand = OrderProductSaveCommand.from(orderProcessCommand, createOrderResult);
        OrderProductSaveResult orderProductList = orderProductService.save(orderProductSaveCommand);

        // 쿠폰 적용
        UserCoupon userCoupon = couponService.useCoupon(orderProcessCommand.getUserId(), orderProcessCommand.getUserCouponId());
        double discountRate = 0.0;
        if(userCoupon != null) {
            discountRate = userCoupon.getCoupon().getDiscountRate();
        }

        // 재고 차감
        orderProductList.getOrderProductDto2List().forEach(product -> {
            productService.reduceStock(product.getProductId(), product.getQuantity());
        });

        // 주문 상태 변경
        ChangeStatusCommand changeStatusCommand = ChangeStatusCommand.from(createOrderResult);
        ChangeStatusResult changedStatusOrder = orderService.changeStatus(changeStatusCommand);

        // 잔액 차감
        balanceService.useBalance(orderProcessCommand.getUserId(), createOrderResult.getTotalPrice(), discountRate);

        // 결제
        PayCommand payCommand = PayCommand.builder()
                        .userId(orderProcessCommand.getUserId())
                        .orderId(createOrderResult.getOrderId())
                        .totalPrice(createOrderResult.getTotalPrice())
                        .discountRate(discountRate)
                        .build();
        paymentService.pay(payCommand);

        return OrderProcessResult.from(changedStatusOrder);
    }
}
