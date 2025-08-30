package kr.hhplus.be.server.domain.payment.application.service;

import kr.hhplus.be.server.domain.coupon.application.event.CouponUsedEvent;
import kr.hhplus.be.server.domain.payment.application.event.PaidEvent;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayCommand;
import kr.hhplus.be.server.domain.payment.application.service.dto.PayResult;
import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import kr.hhplus.be.server.domain.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher publisher;

    public PayResult pay(PayCommand payCommand){

        // 결제 생성
        Payment payment = new Payment();
        payment.create(payCommand);

        // 결제 요청
        Payment createdPayment = paymentRepository.save(payment);
        createdPayment.pay();

        return new PayResult(createdPayment);
    }

    @EventListener
    public void handleCouponUsedEvent(CouponUsedEvent event){
        PayCommand payCommand = PayCommand.from(event);
        pay(payCommand);

        publisher.publishEvent(new PaidEvent(event));
    }

    @Cacheable(value = "topOrderIds", key = "'top::orderIds'")
    public List<Long> getPaidOrderIdsWithinLastDays(int days) {
        List<Payment> paidOrder = paymentRepository.findOrderIdByStatusAndPaidAtAfter("PAID", new Timestamp(System.currentTimeMillis() - Duration.ofDays(days).toMillis()));
        return paidOrder.stream()
                .map(Payment::getOrderId)
                .toList();

    }

}
