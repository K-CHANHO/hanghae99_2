package kr.hhplus.be.server.domain.product.application.facade;

import kr.hhplus.be.server.common.RedisHealthChecker;
import kr.hhplus.be.server.domain.order.application.service.OrderProductService;
import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.GetOrderProductsByOrderIdsResult;
import kr.hhplus.be.server.domain.payment.application.service.PaymentService;
import kr.hhplus.be.server.domain.product.application.facade.dto.GetTopProductsResult;
import kr.hhplus.be.server.domain.product.application.service.ProductRankingService;
import kr.hhplus.be.server.domain.product.application.service.ProductService;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductsCommand;
import kr.hhplus.be.server.domain.product.application.service.dto.GetProductsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final PaymentService paymentService;
    private final OrderProductService orderProductService;
    private final ProductService productService;
    private final ProductRankingService productRankingService;

    private final RedisHealthChecker redisHealthChecker;

    public GetTopProductsResult getTopProducts() {

        GetProductsResult getProductsResult;

        if(redisHealthChecker.isRedisAlive()){
            List<Long> topProductIds = productRankingService.getTopProductIdsLastNDays(3, 5);
            if(topProductIds != null && !topProductIds.isEmpty()) {
                GetProductsCommand getProductsCommand = GetProductsCommand.builder().productIds(topProductIds).build();
                getProductsResult = productService.getProducts(getProductsCommand);
                return GetTopProductsResult.from(getProductsResult);
            }
        }

        // 결제 상태가 PAID이면서 3일 안에 결제된 orderId 조회 (최근 3일)
        List<Long> orderIds = paymentService.getPaidOrderIdsWithinLastDays(3);

        // orderId로 orderProduct 조회하면서 productId로 집계 (top5)
        GetOrderProductsByOrderIdsCommand getOrderProductsByOrderIdsCommand = GetOrderProductsByOrderIdsCommand.from(orderIds);
        GetOrderProductsByOrderIdsResult getOrderProductsByOrderIdsResult = orderProductService.getOrderProductsByOrderIds(getOrderProductsByOrderIdsCommand);

        // productId로 product 조회 (top5)
        GetProductsCommand getProductsCommand = GetProductsCommand.from(getOrderProductsByOrderIdsResult);
        getProductsResult = productService.getProducts(getProductsCommand);

        return GetTopProductsResult.from(getProductsResult);
    }
}
