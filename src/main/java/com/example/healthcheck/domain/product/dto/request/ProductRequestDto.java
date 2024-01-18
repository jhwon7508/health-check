package com.example.healthcheck.domain.product.dto.request;

import com.example.healthcheck.domain.product.entity.Product;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ProductRequestDto {

    @NotNull
    private String productCode;

    @NotNull
    private String productName;

    @NotNull
    private Long price;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private String category;

    private String description;

    public Product convertToEntity(ProductRequestDto productDto) {
        Product product = new Product();
        product.setProductCode(productDto.getProductCode());
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        return product;
    }

}