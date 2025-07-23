package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.domain.balance.entity.Balance;
import kr.hhplus.be.server.domain.balance.repository.BalanceRepository;
import kr.hhplus.be.server.domain.balance.service.BalanceService;
import kr.hhplus.be.server.domain.coupon.entity.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.service.ProductService;
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
    private OrderProductService orderProductService;
    @Autowired
    private ProductService productService;

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ProductRepository productRepository;
    @BeforeEach
    public void initBalance(){
        Balance balance = Balance.builder().userId("sampleUserId").balance(1000000).build();
        balanceRepository.save(balance);

        Product product1 = Product.builder().productName("Product 1").price(10000).build();
        Product product2 = Product.builder().productName("Product 2").price(20000).build();
        Product product3 = Product.builder().productName("Product 3").price(30000).build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

    }
    @Test
    public void orderProcess(){
        // given
        String userId = "sampleUserId";
        ArrayList<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto1 = OrderProductDto.builder().productId(1L).price(10000).quantity(1).build();
        OrderProductDto orderProductDto2 = OrderProductDto.builder().productId(2L).price(20000).quantity(2).build();
        OrderProductDto orderProductDto3 = OrderProductDto.builder().productId(3L).price(30000).quantity(3).build();
        orderProductDtoList.add(orderProductDto1);
        orderProductDtoList.add(orderProductDto2);
        orderProductDtoList.add(orderProductDto3);

        Long couponId = 1L;

        // when
        // 주문 생성
        Order order = orderService.createOrder(userId, orderProductDtoList);

        // 주문 상품 조회
        ArrayList<OrderProduct> orderProductList = new ArrayList<>();
        for( int i = 0 ; i < orderProductDtoList.size(); i++){
            Product product = productService.getProduct(orderProductDtoList.get(i).getProductId());
            OrderProduct orderProduct = OrderProduct.builder()
                    .order(order)
                    .productId(orderProductDtoList.get(i).getProductId())
                    .quantity(orderProductDtoList.get(i).getQuantity())
                    .price(orderProductDtoList.get(i).getPrice())
                    .build();
            orderProductList.add(orderProduct);
        }

        // 주문 상품저장
        orderProductService.save(userId, order.getOrderId(), orderProductList);

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
