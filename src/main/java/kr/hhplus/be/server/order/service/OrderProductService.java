package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.entity.OrderProduct;
import kr.hhplus.be.server.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public List<OrderProduct> save(Long orderId, ArrayList<OrderProduct> productList) {
        return orderProductRepository.saveAll(productList);
    }
}
