package kr.hhplus.be.server.domain.external.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockUseCase {

    //@EventListener
    //@Async
    @KafkaListener(topics = "order-completed", groupId = "hhplus")
    public void handleCompletedOrder(String event) {
        // Mock 처리 로직
        log.info("데이터 플랫폼 수행 ! orderId: {}", event);
    }
}
