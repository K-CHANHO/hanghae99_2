package kr.hhplus.be.server.domain.coupon.application.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.application.service.CouponService;
import kr.hhplus.be.server.domain.coupon.application.service.dto.IssueCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponEventConsumer {

    private final CouponService couponService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon-issue")
    public void consume(String key, String message) throws JsonProcessingException {
        IssueCouponCommand command = objectMapper.readValue(message, IssueCouponCommand.class);
        couponService.issueCoupon(command);
    }
}
