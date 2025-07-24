package kr.hhplus.be.server.product.facade;

import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.repository.OrderProductRepository;
import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductFacadeTest {
    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderProductService orderProductService;
    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductStockRepository productStockRepository;

    // 인기 상품 조회 테스트
    @Test
    @DisplayName("상위 상품 조회 테스트")
    public void getTopProducts(){
        // given
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(OrderProduct.builder().productId(1L).build());
        when(paymentRepository.findOrderIdByStatusAndPaidAtAfter(eq("PAID"), any(Timestamp.class))).thenReturn(Optional.of(new ArrayList<Long>()));
        when(orderProductRepository.findTop5OrderProducts(any(List.class))).thenReturn(orderProductList);
        when(productRepository.findAllById(any(List.class))).thenReturn(List.of(Product.builder().productId(1L).productName("샘플").price(10000).build()));

        // when
        // 결제 상태가 PAID이면서 3일 안에 결제된 orderId 조회 (최근 3일)
        List<Long> orderIds = paymentService.getPaidOrdersWithinLastDays(3);

        // orderId로 orderProduct 조회하면서 productId로 집계 (top5)
        List<Long> orderProductIds = orderProductService.getOrderProductsByOrderIds(orderIds);

        // productId로 product 조회 (top5)
        List<Product> productList = productService.getProducts(orderProductIds);

        // then
        assertThat(productList).isNotEmpty();
        assertThat(productList.size()).isLessThanOrEqualTo(1);
    }
}
