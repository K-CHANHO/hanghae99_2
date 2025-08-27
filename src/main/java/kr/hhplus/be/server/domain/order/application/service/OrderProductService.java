package kr.hhplus.be.server.domain.order.application.service;

import kr.hhplus.be.server.domain.order.application.event.OrderCreatedEvent;
import kr.hhplus.be.server.domain.order.application.service.dto.*;
import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.domain.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ApplicationEventPublisher publisher;

    public OrderProductSaveResult save(OrderProductSaveCommand orderProductSaveCommand) {

        List<OrderProduct> orderProductList = orderProductSaveCommand.getOrderProductDtoList().stream()
                .map(orderProductDto -> OrderProduct.builder()
                        .orderId(orderProductSaveCommand.getOrderId())
                        .productId(orderProductDto.getProductId())
                        .quantity(orderProductDto.getQuantity())
                        .price(orderProductDto.getPrice())
                        .build())
                .toList();

        List<OrderProduct> orderProducts = orderProductRepository.saveAll(orderProductList);
        return OrderProductSaveResult.from(orderProducts);
    }

    // 주문 생성 이벤트 리스너
    @EventListener
    public void handleOrderCompletedEvent(OrderCreatedEvent event) {
        OrderProductSaveCommand orderProductSaveCommand = OrderProductSaveCommand.from(event);
        OrderProductSaveResult orderProductSaveResult = save(orderProductSaveCommand);
    }

    @Cacheable(value = "topProductIds", key = "'top::productIds'")
    public GetOrderProductsByOrderIdsResult getOrderProductsByOrderIds(GetOrderProductsByOrderIdsCommand orderIdsCommand) {
        List<Long> top5OrderProducts = orderProductRepository.findTop5OrderProducts(orderIdsCommand.getOrderIds());

        return GetOrderProductsByOrderIdsResult.from(top5OrderProducts);
    }
}
