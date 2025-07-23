package kr.hhplus.be.server.payment.service;

import kr.hhplus.be.server.payment.entity.Payment;
import kr.hhplus.be.server.payment.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제 생성 테스트")
    public void createPayment(){
        // given
        String userId = "sampleUserId";
        Long orderId = 1L;
        int totalPrice = 100000;
        double discountRate = 0.1;
        when(paymentRepository.save(any(Payment.class))).thenReturn(Payment.builder().status("PENDING").paidPrice((int) (totalPrice * (1 - discountRate))).build());

        // when
        Payment payment = paymentService.create(userId, orderId, totalPrice, discountRate);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getStatus()).isEqualTo("PENDING");
        assertThat(payment.getPaidPrice()).isEqualTo((int) (totalPrice * (1 - discountRate)));
    }

    @Test
    @DisplayName("결제 서비스 테스트")
    public void payPayment(){
        // given
        String userId = "sampleUserId";
        Long orderId = 1L;
        Payment beforePayment = Payment.builder()
                .userId(userId)
                .orderId(orderId)
                .status("PENDING")
                .paidPrice(10000)
                .build();
        Payment afterPayment = Payment.builder()
                .userId(userId)
                .orderId(orderId)
                .status("PAID")
                .paidPrice(10000)
                .build();
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(beforePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(afterPayment);

        // when
        Payment payment = paymentService.pay(userId, orderId, 10000, 0.1);

        // then
        assertThat(payment.getStatus()).isEqualTo("PAID");
    }


}
