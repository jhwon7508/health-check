package com.example.healthcheck.domain.user.repository;

import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Long getBalanceByUserId(String userId);
    User findByUserId(String userId);
//    BalanceResponseDTO chargeBalance(BalanceDTO balanceDto);
//    BalanceResponseDTO checkBalance(String userId);
}

//@Repository
//public interface UserRepository{
//    BalanceResponseDTO chargeBalance(BalanceDTO balanceDto);
//    BalanceResponseDTO checkBalance(String userId);
//    Long getCurrentBalance(String user);
//    void deductBalance(String userId, Long orderTotal);
//    User getUserByUserId(String userId);
//    void setupUserBalance(String userId, Long balance);
//}
