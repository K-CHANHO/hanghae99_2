package kr.hhplus.be.server.domain.order.presenter.controller.dto;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderRequest {

    private String userId;
    private Long userCouponId;
    private List<OrderProductDto> orderProductDtoList;

}
