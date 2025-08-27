package kr.hhplus.be.server.domain.order.presenter.controller;

import kr.hhplus.be.server.apidocs.OrderApiDocs;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.domain.order.application.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.application.service.OrderService;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.application.service.dto.CreateOrderResult;
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
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request) {

        //OrderProcessCommand orderProcessCommand = OrderProcessCommand.from(request);
        //OrderProcessResult orderProcessResult = orderFacade.orderProcess(orderProcessCommand);
        //OrderResponse orderResponse = OrderResponse.from(orderProcessResult);

        CreateOrderCommand createOrderCommand = CreateOrderCommand.from(request);
        CreateOrderResult createdOrder = orderService.createOrder(createOrderCommand);
        OrderResponse orderResponse = OrderResponse.from(createdOrder);

        ApiResponse<OrderResponse> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("주문 요청 성공");
        result.setData(orderResponse);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
