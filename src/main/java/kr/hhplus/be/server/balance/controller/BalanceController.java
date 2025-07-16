package kr.hhplus.be.server.balance.controller;

import kr.hhplus.be.server.balance.dto.BalanceChargeRequest;
import kr.hhplus.be.server.balance.dto.BalanceChargeResponse;
import kr.hhplus.be.server.balance.dto.BalanceViewResponse;
import kr.hhplus.be.server.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1")
public class BalanceController {

    @GetMapping("/balance/{userId}")
    public ResponseEntity<ApiResponse<BalanceViewResponse>> getBalance(@PathVariable String userId) {

        BalanceViewResponse balanceViewResponse = new BalanceViewResponse();
        balanceViewResponse.setUserId(userId);
        balanceViewResponse.setBalance(100000);

        ApiResponse<BalanceViewResponse> result = new ApiResponse<>();
        result.setMessage("성공");
        result.setCode(200);
        result.setData(balanceViewResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/balance")
    public ResponseEntity<ApiResponse<BalanceChargeResponse>> updateBalance(@RequestBody BalanceChargeRequest request) {

        BalanceChargeResponse balanceResponse = new BalanceChargeResponse();
        balanceResponse.setUserId(request.getUserId());
        balanceResponse.setNewBalance(100000 + request.getAmount()); // 예시로 100000에 충전 금액을 더함

        ApiResponse<BalanceChargeResponse> result = new ApiResponse<>();
        result.setMessage("잔액 충전 성공");
        result.setCode(200);
        result.setData(balanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
