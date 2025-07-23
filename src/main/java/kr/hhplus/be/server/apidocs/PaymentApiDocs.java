package kr.hhplus.be.server.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.payment.dto.PaymentRequest;
import kr.hhplus.be.server.domain.payment.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentApiDocs {

    @Tag(name = "결제", description = "결제와 관련된 API")
    @Operation(summary = "결제 처리", description = "사용자가 주문을 결제합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID", required = true, in = ParameterIn.HEADER)
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request);
}
