package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.order.service.OrderProductService;
import kr.hhplus.be.server.domain.payment.application.service.PaymentService;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final PaymentService paymentService;
    private final OrderProductService orderProductService;
    private final ProductService productService;

    public List<Product> getTopProducts() {

        // 결제 상태가 PAID이면서 3일 안에 결제된 orderId 조회 (최근 3일)
        List<Long> orderIds = paymentService.getPaidOrderIdsWithinLastDays(3);

        // orderId로 orderProduct 조회하면서 productId로 집계 (top5)
        List<Long> orderProductIds = orderProductService.getOrderProductsByOrderIds(orderIds);

        // productId로 product 조회 (top5)
        List<Product> productList = productService.getProducts(orderProductIds);

        return productList;
    }
}
