package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.repository.OrderProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderProductServiceTest {
    @InjectMocks
    private OrderProductService orderProductService;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Test
    @DisplayName("주문 상품 저장 테스트")
    public void saveOrderProducts(){
        // given
        String userId = "sampleUserId";
        Long orderId = 1L;
        ArrayList<OrderProduct> productList = new ArrayList<>();
        when(orderProductRepository.saveAll(anyList())).thenReturn(new ArrayList<>(3));
        // when
        List<OrderProduct> savedOrderProducts = orderProductService.save(userId, orderId, productList);

        // then
        assertThat(savedOrderProducts).isNotNull();
        assertThat(savedOrderProducts.size()).isEqualTo(productList.size());
    }

    @Test
    void getOrderProductsByOrderIds() {
        // given
        List<Long> orderIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Long> mockProductIds = List.of(101L, 102L, 103L, 104L, 105L);
        when(orderProductRepository.findTop5OrderProducts(orderIds)).thenReturn(mockProductIds);

        // when
        List<Long> topProductIds = orderProductService.getOrderProductsByOrderIds(orderIds);

        // then
        verify(orderProductRepository).findTop5OrderProducts(orderIds);
        assertThat(topProductIds.size()).isLessThanOrEqualTo(5);

    }

}
