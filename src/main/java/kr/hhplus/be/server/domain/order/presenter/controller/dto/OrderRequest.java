package kr.hhplus.be.server.domain.order.presenter.controller.dto;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String userId;
    private Long userCouponId;
    private List<OrderProductDto> orderProductDtoList;

}
