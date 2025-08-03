package kr.hhplus.be.server.domain.order.presenter.controller;

import kr.hhplus.be.server.apidocs.OrderApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.order.application.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessCommand;
import kr.hhplus.be.server.domain.order.application.facade.dto.OrderProcessResult;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderRequest;
import kr.hhplus.be.server.domain.order.presenter.controller.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderApiDocs {

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request) {

        OrderProcessCommand orderProcessCommand = OrderProcessCommand.from(request);
        OrderProcessResult orderProcessResult = orderFacade.orderProcess(orderProcessCommand);
        OrderResponse orderResponse = OrderResponse.from(orderProcessResult);

        ApiResponse<OrderResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("주문/결제 성공");
        result.setData(orderResponse);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
