package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(OrderProduct.builder().productId(1L).build());

        // when
        List<Product> topProducts = productFacade.getTopProducts();

        // then
        verify(paymentService).getPaidOrderIdsWithinLastDays(3);
        assertThat(topProducts).isNotEmpty();
        assertThat(topProducts.size()).isLessThanOrEqualTo(1);
    }
}
