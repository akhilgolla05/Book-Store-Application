package com.bookstore.order_service.clients.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;

    //https://resilience4j.readme.io/docs/getting-started-3#aspect-order
    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback") // default Retry count :3 , waitduration : 500ms
    public Optional<Product> getProductByCode(String code){
        logger.info("Fetching product for code : {}", code);
        var product = restClient.get()
                .uri("/api/products/{code}", code)
                .retrieve()
                .body(Product.class);
        return Optional.ofNullable(product);
    }

    //getProductByCodeFallback(String code, Throwable t) or getProductByCodeFallback(Throwable t)
    Optional<Product> getProductByCodeFallback(String code, Throwable t){
        logger.info("ProductServiceClient :: getProductByCodeFallback for code {}", code);
        return Optional.empty();
    }



//    public Optional<Product> getProductByCode(String code){
//        logger.info("Fetching product for code : {}", code);
//        try {
//            var product = restClient.get()
//                    .uri("/api/products/{code}", code)
//                    .retrieve()
//                    .body(Product.class);
//            return Optional.ofNullable(product);
//        } catch (Exception e) {
//            logger.error("Error fetching product for code : {}", code, e);
//            return Optional.empty();
//        }
//    }

    /*

    2025-09-28T20:38:25.897-05:00  INFO 17488 --- [order-service] [nio-8082-exec-1] c.b.o.c.catalog.ProductServiceClient     : Fetching product for code : P101
2025-09-28T20:38:31.434-05:00  INFO 17488 --- [order-service] [nio-8082-exec-1] c.b.o.c.catalog.ProductServiceClient     : Fetching product for code : P101
2025-09-28T20:38:36.950-05:00  INFO 17488 --- [order-service] [nio-8082-exec-1] c.b.o.c.catalog.ProductServiceClient     : Fetching product for code : P101
2025-09-28T20:38:42.564-05:00  WARN 17488 --- [order-service] [nio-8082-exec-1] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8081/api/products/P101": Read timed out]

     */
}
