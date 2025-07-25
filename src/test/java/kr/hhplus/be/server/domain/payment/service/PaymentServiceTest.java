package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
        Long paymentId = 1L;
        Long orderId = 1L;
        Payment beforePayment = Payment.builder()
                .paymentId(paymentId)
                .userId(userId)
                .orderId(orderId)
                .status("PENDING")
                .paidPrice(10000)
                .build();
        Payment afterPayment = Payment.builder()
                .paymentId(paymentId)
                .userId(userId)
                .orderId(orderId)
                .status("PAID")
                .paidPrice(10000)
                .build();
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(beforePayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(afterPayment);

        // when
        Payment payment = paymentService.pay(userId, paymentId);

        // then
        assertThat(payment.getStatus()).isEqualTo("PAID");
    }


    @Test
    void getPaidOrderIdsWithinLastDays() {
        // given
        int withinDays = 3;
        List<Long> mockOrderIds = List.of(1L, 2L, 3L, 4L, 5L);
        when(paymentRepository.findOrderIdByStatusAndPaidAtAfter(anyString(), any(Timestamp.class))).thenReturn(mockOrderIds);

        // when
        List<Long> orderIds = paymentService.getPaidOrderIdsWithinLastDays(withinDays);

        // then
        verify(paymentRepository).findOrderIdByStatusAndPaidAtAfter(anyString(), any(Timestamp.class));
        assertThat(orderIds).isNotNull();
        assertThat(orderIds.size()).isEqualTo(5);
    }
}
