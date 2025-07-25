package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public List<OrderProduct> save(String userId, Long orderId, List<OrderProductDto> orderProductDtoList) {

        List<OrderProduct> orderProductList = orderProductDtoList.stream()
                .map(orderProductDto -> OrderProduct.builder()
                        .orderId(orderId)
                        .productId(orderProductDto.getProductId())
                        .quantity(orderProductDto.getQuantity())
                        .price(orderProductDto.getPrice())
                        .build())
                .toList();

        return orderProductRepository.saveAll(orderProductList);
    }

    public List<Long> getOrderProductsByOrderIds(List<Long> orderIds) {
        return orderProductRepository.findTop5OrderProducts(orderIds);
    }
}
