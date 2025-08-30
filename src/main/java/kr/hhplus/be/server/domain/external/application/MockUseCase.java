package kr.hhplus.be.server.domain.external.application;

import kr.hhplus.be.server.domain.order.application.event.OrderCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockUseCase {

    @EventListener
    @Async
    public void handleCompletedOrder(OrderCompletedEvent event) {
        // Mock 처리 로직
        log.info("데이터 플랫폼 수행 ! orderId: {}, orderPrice: {}", event.getOrderId(), event.getOrderPrice());
    }
}
