```mermaid
erDiagram
    user{
        string userId PK
        int balance
    }
    order{
        string orderId PK
        string userId FK "user"
        string userCouponId FK "userCoupon"
        int totalPrice
        string status
        string orderDate
    }
    orderProduct{
        string orderProductId PK
        string orderId FK "order"
        string productId FK "product"
        int quantity
        int price
    }
    product{
        string productId PK
        string productName
        int stock
        int price
    }
    userCoupon{
        string userCouponId PK
        string userId FK "user"
        string couponId FK "coupon"
        timestamp issuedAt
    }

    user ||--o{ order : places
    order ||--o{ orderProduct : contains
    order ||--o| userCoupon : relates
    orderProduct ||--o{ product : relates
```