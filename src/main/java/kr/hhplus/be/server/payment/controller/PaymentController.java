package kr.hhplus.be.server.payment.controller;

import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.payment.dto.PaymentRequest;
import kr.hhplus.be.server.payment.dto.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

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
