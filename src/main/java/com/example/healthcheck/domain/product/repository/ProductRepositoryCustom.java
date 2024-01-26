package com.example.healthcheck.domain.product.repository;

import com.example.healthcheck.domain.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {
    List<Product> findDailyPopularProducts();
}
