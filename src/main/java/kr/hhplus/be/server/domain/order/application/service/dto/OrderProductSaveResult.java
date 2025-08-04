package kr.hhplus.be.server.domain.order.application.service.dto;

import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class OrderProductSaveResult {

    private List<OrderProductDto> orderProductDtoList;

    @Builder
    @Getter
    public static class OrderProductDto {
        private Long orderProductId;
        private Long orderId;
        private Long productId;
        private int quantity;
        private int price;
    }

    public static OrderProductDto toDto(OrderProduct orderProduct){
        return OrderProductDto.builder()
                .orderProductId(orderProduct.getOrderProductId())
                .orderId(orderProduct.getOrderId())
                .productId(orderProduct.getProductId())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice())
                .build();
    }

    public static OrderProductSaveResult from(List<OrderProduct> orderProducts) {
        List<OrderProductDto> list = orderProducts.stream().map(OrderProductSaveResult::toDto).toList();

        return OrderProductSaveResult.builder()
                .orderProductDtoList(list)
                .build();
    }

}
