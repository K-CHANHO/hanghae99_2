package kr.hhplus.be.server.domain.order.facade;

import kr.hhplus.be.server.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;

}
