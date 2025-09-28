package com.bookstore.order_service.web.controllers;

import com.bookstore.order_service.AbstractIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

//Integration Test
class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTest{

        @Test
        void shouldCreateOrderSuccessfully(){
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

            RestAssured.given().contentType(ContentType.JSON)
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
            RestAssured.given().contentType(ContentType.JSON)
                    .body(payload)
                    .when().post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors", Matchers.notNullValue());

        }
    }

}