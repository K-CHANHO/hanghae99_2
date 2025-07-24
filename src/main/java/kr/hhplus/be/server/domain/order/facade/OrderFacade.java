package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.balance.service.BalanceService;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Order orderProcess(String userId, List<OrderProductDto> orderProductDtoList, Long couponId) {

        // 주문 생성
        Order order = orderService.createOrder(userId, orderProductDtoList);

        // 주문 상품 조회
        List<Product> productList = orderProductDtoList.stream()
                .map(orderProductDto -> productService.getProduct(orderProductDto.getProductId()))
                .toList();

        // 주문 상품 저장
        List<OrderProduct> orderProductList = orderProductService.save(userId, order.getOrderId(), orderProductDtoList);

        // 쿠폰 적용
        UserCoupon userCoupon = couponService.useCoupon(userId, couponId);
        double discountRate = 0.0;
        if(userCoupon != null) {
            discountRate = userCoupon.getCoupon().getDiscountRate();
        }

        return order;
    }
}
