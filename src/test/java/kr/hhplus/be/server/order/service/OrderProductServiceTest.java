package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.entity.OrderProduct;
import kr.hhplus.be.server.order.repository.OrderProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        Long orderId = 1L;
        ArrayList<Long> productList = new ArrayList<>();
        productList.add(1L);
        productList.add(2L);
        productList.add(3L);
        Mockito.when(orderProductRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<>(3));

        // when
        List<OrderProduct> savedOrderProducts = orderProductService.save(orderId, productList);

        // then
        assertThat(savedOrderProducts).isNotNull();
        assertThat(savedOrderProducts.size()).isEqualTo(productList.size());
    }
}
