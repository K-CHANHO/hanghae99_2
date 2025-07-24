package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Long> findOrderIdByStatusAndPaidAtAfter(String status, Timestamp days);
}
