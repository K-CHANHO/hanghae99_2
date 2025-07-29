package kr.hhplus.be.server.domain.payment.infra;

import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    List<Payment> findOrderIdByStatusAndPaidAtAfter(String status, Timestamp days);
}