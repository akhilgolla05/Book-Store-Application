package com.bookstore.catalog_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "products")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Product Code is Required") private String code;

    @Column(nullable = false)
    @NotEmpty(message = "Product Name is Required") private String name;

    private String description;

    private String imageUrl;

    @NotEmpty(message = "Product Price is Required") @DecimalMin("0.1") @Column(nullable = false)
    private BigDecimal price;
}
