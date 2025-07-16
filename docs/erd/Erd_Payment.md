```mermaid
erDiagram
    user{
        string userId PK
        int balance
    }
    order{
        string orderId PK
        string userId FK "user"
        int totalPrice
        string status
        string orderDate
    }
    payment {
        string paymentId PK
        string userId FK "user"
        string orderId FK "order"
        string userCouponId FK "userCoupon"
        string status
        string price
    }
    userCoupon{
        string userCouponId PK
        string userId FK "user"
        string couponId FK "coupon"
        timestamp issuedAt
    }

    user ||--o{ payment : places
    payment ||--o| order : contains
    payment ||--o| userCoupon : relates

```