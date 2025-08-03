package kr.hhplus.be.server.domain.order.domain.repository;

import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;

import java.util.List;

public interface OrderProductRepository {
    List<Long> findTop5OrderProducts(List<Long> orderIds);

    List<OrderProduct> saveAll(List<OrderProduct> orderProductList);

}
