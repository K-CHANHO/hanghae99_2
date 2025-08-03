package kr.hhplus.be.server.domain.order.application.service;

import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsResult;
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

    public GetOrderProductsByOrderIdsResult getOrderProductsByOrderIds(GetOrderProductsByOrderIdsCommand orderIdsCommand) {
        List<Long> top5OrderProducts = orderProductRepository.findTop5OrderProducts(orderIdsCommand.getOrderIds());

        return GetOrderProductsByOrderIdsResult.from(top5OrderProducts);
    }
}
