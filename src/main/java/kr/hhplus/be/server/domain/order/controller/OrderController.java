package kr.hhplus.be.server.domain.order.controller;

import kr.hhplus.be.server.apidocs.OrderApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.order.dto.OrderRequest;
import kr.hhplus.be.server.domain.order.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController implements OrderApiDocs {

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request) {

        OrderResponse response = new OrderResponse();
        response.setOrderId("12345");
        response.setUserId("sampleUserId");
        response.setTotalPrice(600000);
        response.setStatus("CREATED");
        response.setOrderDate("2023-10-01T12:00:00Z");

        ApiResponse<OrderResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("주문 성공");
        result.setData(response);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
