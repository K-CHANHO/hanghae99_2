package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment create(String userId, Long orderId, int totalPrice, double discountRate){
        Payment payment = new Payment();
        payment.create(userId, orderId, totalPrice, discountRate);
        return paymentRepository.save(payment);
    }

    public Payment pay(String userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("유효하지 않은 주문입니다."));
        payment.pay();

        return paymentRepository.save(payment);
    }

    public Payment pay(Payment payment) {
        payment.pay();
        return paymentRepository.save(payment);
    }

    public List<Long> getPaidOrderIdsWithinLastDays(int days) {
        return paymentRepository.findOrderIdByStatusAndPaidAtAfter("PAID", new Timestamp(System.currentTimeMillis() - Duration.ofDays(days).toMillis()));

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPaymentNotification(Payment payment) {
        System.out.println("결제정보 외부 전송");
    }


}
