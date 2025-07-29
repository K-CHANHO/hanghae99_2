package kr.hhplus.be.server.domain.order.infra;

import kr.hhplus.be.server.domain.order.domain.entity.OrderProduct;
import kr.hhplus.be.server.domain.order.domain.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepository {

    private final OrderProductJpaRepository jpaRepository;

    @Override
    public List<Long> findTop5OrderProducts(List<Long> orderIds) {
        return jpaRepository.findTop5OrderProducts(orderIds);
    }

    @Override
    public List<OrderProduct> saveAll(List<OrderProduct> orderProductList) {
        return jpaRepository.saveAll(orderProductList);
    }
}
