package com.example.healthcheck.domain.user.service;

import com.example.healthcheck.domain.common.enums.ResponseCode;
import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.user.entity.User;
import com.example.healthcheck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserRepository userRepository;


    public BalanceResponseDTO chargeBalance(BalanceDTO balanceDto) {
        try {
            User user = userRepository.findByUserId(balanceDto.getUserId());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseCode.CODE_0001.toString());
            }
            Long originalBalance = user.getBalance();
            Long updatedBalance = originalBalance + balanceDto.getBalance();

            user.setBalance(updatedBalance);

            return BalanceResponseDTO.builder()
                    .userId(balanceDto.getUserId())
                    .originalBalance(originalBalance)
                    .updatedBalance(updatedBalance)
                    .build();
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }

    public BalanceResponseDTO checkBalance(String userId) {
        try {
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseCode.CODE_0001.toString());
            }
            Long balance = user.getBalance();

            return BalanceResponseDTO.builder()
                    .userId(userId)
                    .originalBalance(balance)
                    .build();
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }
}

