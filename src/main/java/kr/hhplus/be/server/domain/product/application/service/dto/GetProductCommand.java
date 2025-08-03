package kr.hhplus.be.server.domain.product.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetProductCommand {

    private Long productId;

    public static GetProductCommand from(Long productId) {
        return GetProductCommand.builder()
                .productId(productId)
                .build();
    }
}
