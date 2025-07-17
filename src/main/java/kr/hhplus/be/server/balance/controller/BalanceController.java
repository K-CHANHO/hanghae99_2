package kr.hhplus.be.server.balance.controller;

import kr.hhplus.be.server.balance.dto.ChargeBalanceRequest;
import kr.hhplus.be.server.balance.dto.ChargeBalanceResponse;
import kr.hhplus.be.server.balance.dto.ViewBalanceResponse;
import kr.hhplus.be.server.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceController {

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ViewBalanceResponse>> getBalance(@PathVariable String userId) {

        ViewBalanceResponse viewBalanceResponse = new ViewBalanceResponse();
        viewBalanceResponse.setUserId(userId);
        viewBalanceResponse.setBalance(100000);

        ApiResponse<ViewBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 조회 성공");
        result.setCode(200);
        result.setData(viewBalanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ChargeBalanceResponse>> updateBalance(@RequestBody ChargeBalanceRequest request) {

        ChargeBalanceResponse balanceResponse = new ChargeBalanceResponse();
        balanceResponse.setUserId(request.getUserId());
        balanceResponse.setNewBalance(100000 + request.getAmount()); // 예시로 100000에 충전 금액을 더함

        ApiResponse<ChargeBalanceResponse> result = new ApiResponse<>();
        result.setMessage("잔액 충전 성공");
        result.setCode(200);
        result.setData(balanceResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
