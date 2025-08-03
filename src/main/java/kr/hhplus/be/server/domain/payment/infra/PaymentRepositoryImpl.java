package kr.hhplus.be.server.domain.payment.infra;

import kr.hhplus.be.server.domain.payment.domain.entity.Payment;
import kr.hhplus.be.server.domain.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    @Override
    public List<Payment> findOrderIdByStatusAndPaidAtAfter(String status, Timestamp days) {
        return jpaRepository.findOrderIdByStatusAndPaidAtAfter(status, days);
    }

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }
}
