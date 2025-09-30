package com.bookstore.order_service;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for integration Test : loading entire components
public abstract class AbstractIT {

    @LocalServerPort
    int port;
    //creating a static wiremock container instance
    static WireMockContainer mockContainer = new WireMockContainer("wiremock/wiremock:3.6.0-alpine");

    @BeforeAll
    static void beforeAll() {
        mockContainer.start(); // starting the wiremock server and setting the default host and port
        configureFor(mockContainer.getHost(), mockContainer.getPort());
    }

    //instead of talking to the catalog-service when calling the URL, it talks to the wiremock server base URL
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url",mockContainer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        stubFor(WireMock.get(WireMock.urlMatching("/api/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(
                                """
                    {
                        "code": "%s",
                        "name": "%s",
                        "price": %f
                    }
                """
                                        .formatted(code, name, price.doubleValue()))));
    }
}
