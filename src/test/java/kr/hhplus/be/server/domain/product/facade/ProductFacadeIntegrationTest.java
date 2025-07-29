package kr.hhplus.be.server.domain.product.facade;

import kr.hhplus.be.server.domain.product.application.facade.ProductFacade;
import kr.hhplus.be.server.domain.product.domain.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql(scripts = {
        "/testSql/cleanup.sql",
        "/testSql/order.sql",
        "/testSql/orderProduct.sql",
        "/testSql/payment.sql",
        "/testSql/product.sql"
})
public class ProductFacadeIntegrationTest {
    @Autowired
    private ProductFacade productFacade;

    @Test
    @DisplayName("상위 상품 조회 테스트")
    public void getTopProducts(){
        // given

        // when
        List<Product> topProducts = productFacade.getTopProducts();

        // then
        Assertions.assertThat(topProducts).isNotEmpty();
        Assertions.assertThat(topProducts).hasSizeLessThanOrEqualTo(5);
    }
}
