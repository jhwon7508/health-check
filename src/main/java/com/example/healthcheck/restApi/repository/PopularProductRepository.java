package com.example.healthcheck.restApi.repository;

import com.example.healthcheck.domain.entity.PopularProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PopularProductRepository extends JpaRepository<PopularProduct, Long>, JpaSpecificationExecutor<PopularProduct> {
}
