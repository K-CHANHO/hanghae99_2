package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFacade {
    private final PaymentService paymentService;
    private final OrderService orderService;
}
