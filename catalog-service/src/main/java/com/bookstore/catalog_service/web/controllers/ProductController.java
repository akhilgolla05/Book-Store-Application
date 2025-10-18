package com.bookstore.catalog_service.web.controllers;

import com.bookstore.catalog_service.domain.PagedResult;
import com.bookstore.catalog_service.domain.Product;
import com.bookstore.catalog_service.domain.ProductNotFoundException;
import com.bookstore.catalog_service.domain.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Slf4j
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        log.info("ProductController :: getProducts ");
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        // sleep(); //for timeout
        log.info("ProductController :: getProductByCode : {} ", code);
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }

    void sleep() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {

        }
    }
}
