package kr.hhplus.be.server.domain.payment.domain.repository;

import kr.hhplus.be.server.domain.payment.domain.entity.Payment;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentRepository {
    List<Payment> findOrderIdByStatusAndPaidAtAfter(String status, Timestamp days);

    Payment save(Payment payment);
}
