package com.bookstore.catalog_service.web.controllers;

import com.bookstore.catalog_service.AbstractIT;
import com.bookstore.catalog_service.domain.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@Sql("/test-data.sql") //it executes SQL script before every test execution method
class ProductControllerTest extends AbstractIT {

    @Test
    void shouldReturnAllProducts() {
        RestAssured.given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements",is(15))
                .body("pageNumber",is(1))
                .body("totalPages",is(2))
                .body("isFirst",is(true))
                .body("isLast",is(false))
                .body("hasNext",is(true))
                .body("hasPrevious",is(false));

    }

    @Test
    void shouldGetProductByCode(){
        Product product = RestAssured.given().contentType(ContentType.JSON)
                .when().get("/api/products/{code}", "P100")
                .then()
                .statusCode(200)
                .assertThat()
                .extract().body().as(Product.class);

        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.name()).isEqualTo("The Hunger Games");
        assertThat(product.price()).isEqualTo(new BigDecimal("34.0"));
        assertThat(product.description()).isEqualTo("Winning will make you famous. Losing means certain death...");
    }

    @Test
    void shouldReturnNotFoundWhenProductCodeNotExists(){
        String code = "P900";
        RestAssured.given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", code)
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("title", is("Product Not Found"))
                .body("detail", is("Product Not Found With the Code : "+ code));

    }


}