package com.example.healthcheck.restApi.repository;

import com.example.healthcheck.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
