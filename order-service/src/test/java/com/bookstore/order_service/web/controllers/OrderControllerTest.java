package com.bookstore.order_service.web.controllers;

import com.bookstore.order_service.AbstractIT;
import com.bookstore.order_service.domain.models.OrderDto;
import com.bookstore.order_service.domain.models.OrderSummary;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;


//Integration Test
@Sql(scripts = "/test-orders.sql")//for every test method this SQL script gonna run
class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTest{

        @Test
        void shouldCreateOrderSuccessfully(){
            mockGetProductByCode("P100","product 1", new BigDecimal("25.50"));
            mockGetProductByCode("P101","product 2", new BigDecimal("35.50"));
            String payload = """
                    {
                        "customer":{
                            "name":"Munna",
                            "email":"munna@gmail.com",
                            "phone":"8545712558"
                        },
                        "deliveryAddress":{
                            "addressLine1":"222 kukatpally",
                            "addressLine2":"kphb",
                            "city":"Hyderabad",
                            "state":"Telangana",
                            "zipCode":"500072",
                            "country":"India"
                        },
                        "items":[
                            {
                                "code":"P100",
                                "name":"product 1",
                                "price":"25.50",
                                "quantity":1
                            },
                            {
                                 "code":"P101",
                                 "name":"product 2",
                                 "price":35.50,
                                 "quantity":2
                            }
                        ]
                    }
                    """;

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when().post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", Matchers.notNullValue());
        }

        @Test
        void shoudReturnBadRequestWhenMandatoryDataMissing(){
            var payload = """
                    {
                        "customer":{
                            "name":"Munna",
                            "email":"munna@gmail.com",
                            "phone":"8545712558"
                        },
                        "deliveryAddress":{
                            "addressLine1":"222 kukatpally",
                            "addressLine2":"kphb",
                            "city":"Hyderabad",
                            "state":"Telangana",
                            "zipCode":"500072",
                            "country":""
                        },
                        "items":[
                            {
                                "code":"P100",
                                "name":"product 1",
                                "price":"25.50",
                                "quantity":1
                            },
                            {
                                "code":"P101",
                                "name":"product 2",
                                "price":"35.50",
                                "quantity":2
                            },
                            {
                                "code":"P102",
                                "name":"product 3",
                                "price":"45.50",
                                "quantity":5
                            }
                        ]
                    }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when().post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", Matchers.notNullValue());

        }
    }

//    @Nested
//    class GetOrdersTest{
//        @Test
//        void shouldGetOrdersSuccessfully(){
//            List<OrderSummary> orderSummaryList = RestAssured.given()
//                    .when().get("/api/orders")
//                    .then()
//                    .statusCode(200)
//                    .extract().body()
//                    .as(new TypeRef<>() {});
//
//            System.out.println(orderSummaryList);
//            assertThat(orderSummaryList).hasSize(2);
//        }
//    }


//    @Nested
//    class getOrderByOrderNumberTest{
//
//        String orderNumber = "order-123";
//        @Test
//        void getOrderByOrderNumberSuccessfully(){
//
//            OrderDto orderDto = given()
//                    .when().get("/api/orders/{orderNumber}", orderNumber)
//                    .then().statusCode(HttpStatus.OK.value())
//                    .body("orderNumber", is(orderNumber))
//                    .body("items.size()", is(2))
//                    .extract().body().as(OrderDto.class);
//
//            assertThat(orderDto).isNotNull();
//
//        }
//    }

}