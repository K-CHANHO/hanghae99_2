package kr.hhplus.be.server.domain.balance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewBalanceRequest {
    private String userId;
}
