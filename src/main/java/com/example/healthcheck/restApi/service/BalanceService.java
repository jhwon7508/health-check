package com.example.healthcheck.restApi.service;

import com.example.healthcheck.domain.dto.request.BalanceDTO;
import com.example.healthcheck.domain.dto.response.BalanceResponseDTO;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.repository.UserRepository;
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
            BalanceResponseDTO result = userRepository.chargeBalance(balanceDto);
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }

    public BalanceResponseDTO checkBalance(String userId) {
        try {
            BalanceResponseDTO result = userRepository.checkBalance(userId);
            return result;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_0002.toString());
        }
    }
}

