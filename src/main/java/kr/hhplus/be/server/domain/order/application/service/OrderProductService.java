package kr.hhplus.be.server.domain.order.application.service;

import kr.hhplus.be.server.domain.order.application.service.dto.OrderProductSaveCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.OrderProductSaveResult;
import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.domain.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

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

    // TODO : dto 분리
    public List<Long> getOrderProductsByOrderIds(List<Long> orderIds) {
        return orderProductRepository.findTop5OrderProducts(orderIds);
    }
}
