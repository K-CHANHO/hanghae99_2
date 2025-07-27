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

    public Payment pay(String userId, Long orderId, int totalPrice, double discountRate){
        // 결제 생성
        Payment payment = new Payment();
        payment.create(userId, orderId, totalPrice, discountRate);

        // 결제 요청
        Payment createdPayment = paymentRepository.save(payment);
        createdPayment.pay();

        // 알림 전송
        sendPaymentNotification(createdPayment);

        return createdPayment;
    }

    public List<Long> getPaidOrderIdsWithinLastDays(int days) {
        return paymentRepository.findOrderIdByStatusAndPaidAtAfter("PAID", new Timestamp(System.currentTimeMillis() - Duration.ofDays(days).toMillis()));

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void sendPaymentNotification(Payment payment) {
        System.out.println("결제정보 외부 전송");
    }


}
