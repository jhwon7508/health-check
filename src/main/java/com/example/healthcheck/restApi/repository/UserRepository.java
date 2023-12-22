package com.example.healthcheck.restApi.repository;

import com.example.healthcheck.domain.dto.request.BalanceDTO;
import com.example.healthcheck.domain.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.entity.User;
import org.springframework.stereotype.Repository;

//public interface UserRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
//}

@Repository
public interface UserRepository{
    BalanceResponseDTO chargeBalance(BalanceDTO balanceDto);
    BalanceResponseDTO checkBalance(String userId);
    Long getCurrentBalance(String user);
    void deductBalance(String userId, Long orderTotal);

    User getUserByUserId(String userId);
}
