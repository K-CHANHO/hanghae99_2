package kr.hhplus.be.server.payment.service;

import kr.hhplus.be.server.payment.entity.Payment;
import kr.hhplus.be.server.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
