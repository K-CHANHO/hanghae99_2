package kr.hhplus.be.server.balance.controller;

import kr.hhplus.be.server.balance.dto.BalanceViewResponse;
import kr.hhplus.be.server.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
