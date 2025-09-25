package com.bookstore.catalog_service.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
})
//@Import(TestcontainersConfiguration.class) : not recommended for repo layers having a single DB
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(15);
    }

    @Test
    void shouldGetProductByCode(){
        ProductEntity product = productRepository.findByCode("P101").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P101");
        assertThat(product.getName()).isEqualTo("To Kill a Mockingbird");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("45.40"));
    }

//    @Test
//    void shouldFailProductWithDuplicateCode(){
//        var product = new ProductEntity(null, "P101", "To Kill a Mockingbird","","",BigDecimal.ZERO);
//        assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(product));
//    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExists(){
        assertThat(productRepository.findByCode("P900")).isEmpty();
    }
}