package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.application.service.OrderProductService;
import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsResult;
import kr.hhplus.be.server.domain.order.application.service.dto.OrderProductSaveCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.OrderProductSaveResult;
import kr.hhplus.be.server.domain.order.domain.repository.OrderProductRepository;
import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
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
        ArrayList<OrderProductDto> productList = new ArrayList<>();
        OrderProductSaveCommand orderProductSaveCommand = OrderProductSaveCommand.builder()
                        .userId(userId)
                        .orderId(orderId)
                        .orderProductDtoList(productList)
                        .build();
        when(orderProductRepository.saveAll(anyList())).thenReturn(new ArrayList<>(3));

        // when
        OrderProductSaveResult orderProductSaveResult = orderProductService.save(orderProductSaveCommand);

        // then
        assertThat(orderProductSaveResult.getOrderProductDtoList()).isNotNull();
        assertThat(orderProductSaveResult.getOrderProductDtoList().size()).isEqualTo(productList.size());
    }

    @Test
    void getOrderProductsByOrderIds() {
        // given
        List<Long> orderIds = List.of(1L, 2L, 3L, 4L, 5L);
        GetOrderProductsByOrderIdsCommand orderIdsCommand = GetOrderProductsByOrderIdsCommand.from(orderIds);
        List<Long> mockProductIds = List.of(101L, 102L, 103L, 104L, 105L);
        when(orderProductRepository.findTop5OrderProducts(orderIds)).thenReturn(mockProductIds);

        // when
        GetOrderProductsByOrderIdsResult orderProductsByOrderIds = orderProductService.getOrderProductsByOrderIds(orderIdsCommand);

        // then
        verify(orderProductRepository).findTop5OrderProducts(orderIds);
        assertThat(orderProductsByOrderIds.getProductIds().size()).isLessThanOrEqualTo(5);

    }

}
