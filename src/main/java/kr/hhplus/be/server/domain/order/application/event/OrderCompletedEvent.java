package kr.hhplus.be.server.domain.order.application.event;

public class OrderCompletedEvent {
    private final Long orderId;
    private final int orderPrice;

    public OrderCompletedEvent(Long orderId, int orderPrice) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }
}
