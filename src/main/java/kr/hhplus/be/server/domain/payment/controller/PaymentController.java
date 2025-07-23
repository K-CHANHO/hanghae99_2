package kr.hhplus.be.server.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.payment.dto.PaymentRequest;
import kr.hhplus.be.server.domain.payment.dto.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Tag(name = "결제", description = "결제와 관련된 API")
    @Operation(summary = "결제 처리", description = "사용자가 주문을 결제합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "결제 성공")
    @Parameter(name = "transactionId", description = "트랜잭션 ID", required = true, in = ParameterIn.HEADER)
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("payment1");
        response.setOrderId("ORDER1");
        response.setUserId("sampleUser");
        response.setPrice("500000");

        ApiResponse<PaymentResponse> result = new ApiResponse<>();
        result.setMessage("결제 성공");
        result.setCode(200);
        result.setData(response);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
