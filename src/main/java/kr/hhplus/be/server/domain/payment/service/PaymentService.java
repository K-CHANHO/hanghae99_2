package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void sendPaymentNotification(Payment payment) {
        System.out.println("결제정보 외부 전송");
    }

    public List<Long> getPaidOrdersWithinLastDays(int i) {
        return paymentRepository.findOrderIdByStatusAndPaidAtAfter("PAID", System.currentTimeMillis() - i * 24 * 60 * 60 * 1000L)
                .orElseThrow(() -> new RuntimeException("최근 " + i + "일 이내에 결제된 주문이 없습니다."));
    }
}
