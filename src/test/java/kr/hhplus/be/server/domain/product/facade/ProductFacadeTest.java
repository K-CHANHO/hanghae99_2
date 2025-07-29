package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.payment.application.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductFacadeTest {
    @InjectMocks
    private ProductFacade productFacade;
    @Mock
    private PaymentService paymentService;
    @Mock
    private OrderProductService orderProductService;
    @Mock
    private ProductService productService;


    // 인기 상품 조회 테스트
    @Test
    @DisplayName("상위 상품 조회 테스트")
    public void getTopProducts(){
        // given
        List<Long> mockOrderIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Long> mockProductIds = List.of(101L, 102L, 103L, 104L, 105L);
        List<Product> mockProducts = List.of(
                Product.builder().productId(1L).productName("Product 1").price(10000).build(),
                Product.builder().productId(2L).productName("Product 2").price(20000).build(),
                Product.builder().productId(3L).productName("Product 3").price(30000).build(),
                Product.builder().productId(4L).productName("Product 4").price(40000).build(),
                Product.builder().productId(5L).productName("Product 5").price(50000).build()
        );
        when(paymentService.getPaidOrderIdsWithinLastDays(3)).thenReturn(mockOrderIds);
        when(orderProductService.getOrderProductsByOrderIds(mockOrderIds)).thenReturn(mockProductIds);
        when(productService.getProducts(mockProductIds)).thenReturn(mockProducts);

        // when
        List<Product> topProducts = productFacade.getTopProducts();

        // then
        verify(paymentService).getPaidOrderIdsWithinLastDays(anyInt());
        verify(orderProductService).getOrderProductsByOrderIds(anyList());
        verify(productService).getProducts(anyList());
        assertThat(topProducts).isNotEmpty();
        assertThat(topProducts.size()).isLessThanOrEqualTo(5);
    }
}
