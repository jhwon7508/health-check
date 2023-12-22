package com.example.healthcheck.restApi.repository;

import com.example.healthcheck.domain.dto.request.OrderDTO;
import com.example.healthcheck.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
//}

@Repository
public interface PaymentRepository{

    void processPayment(Object any);

    void sendOrderData(OrderDTO orderDTO);
}