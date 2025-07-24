package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public List<OrderProduct> save(String userId, Long orderId, ArrayList<OrderProduct> productList) {

        return orderProductRepository.saveAll(productList);
    }

    public List<Long> getOrderProductsByOrderIds(List<Long> orderIds) {
        return orderProductRepository.findTop5OrderProducts(orderIds);
    }
}
