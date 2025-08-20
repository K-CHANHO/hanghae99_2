package kr.hhplus.be.server.domain.order.presenter.controller.dto;

import kr.hhplus.be.server.domain.order.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String userId;
    private Long userCouponId;
    private List<OrderProductDto> orderProductDtoList;

}
