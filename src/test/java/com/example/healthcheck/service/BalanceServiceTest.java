package com.example.healthcheck.service;

import com.example.healthcheck.domain.dto.request.BalanceDTO;
import com.example.healthcheck.domain.dto.response.BalanceResponseDTO;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.repository.UserRepository;
import com.example.healthcheck.restApi.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private UserRepository userRepository;

    private BalanceService balanceService;

    // 각 테스트 실행 전에 실행되는 메소드
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        balanceService = new BalanceService(userRepository);
    }

    // 데이터베이스 에러 시나리오에 대한 테스트 메소드.
    @Test
    void chargeBalanceDatabaseError() {
        // 테스트용 BalanceDTO 객체 생성.
        BalanceDTO balanceDto = new BalanceDTO("user123", 5000L);

        // userRepository의 메소드를 모킹하여, 특정 조건에서 DataAccessException 예외를 던지도록 설정
        when(userRepository.chargeBalance(balanceDto)).thenThrow(new DataAccessException(ResponseCode.CODE_0002.getMessage()) {
        });

        // 예외가 발생하는지 확인하는 테스트. 예외가 발생하면 테스트는 성공
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            balanceService.chargeBalance(balanceDto);
        });

        // 예외 메시지가 기대한 메시지를 포함하는지 확인
        String expectedMessage = "데이터베이스 오류가 발생했습니다";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // 잔액 충전 시나리오에 대한 테스트
    @Test
    public void testChargeBalance() {
        // 테스트 데이터
        String user = "user123";
        Long amount = 10000L;

        BalanceDTO balanceDto = BalanceDTO.builder()
                .userId(user)
                .balance(amount)
                .build();

        BalanceResponseDTO expected = BalanceResponseDTO.builder()
                .userId(user)
                .originalBalance(amount)
                .updatedBalance(amount + 5000L) // 잔액이 5000L 증가했다고 가정
                .build();

        // userRepository의 메소드 모킹
        when(userRepository.getCurrentBalance(user)).thenReturn(expected.getUpdatedBalance());
        when(userRepository.chargeBalance(any(BalanceDTO.class))).thenReturn(expected);

        // 실제 서비스 메소드 호출 및 결과 검증.
        BalanceResponseDTO result = balanceService.chargeBalance(balanceDto);
        assertEquals(expected, result);

        // 잔액 조회 로직 확인
        Long currentBalance = userRepository.getCurrentBalance(user);
        assertEquals(expected.getUpdatedBalance(), currentBalance);
    }

    // 잔액 조회 시나리오에 대한 테스트
    @Test
    public void testCheckBalance() {
        // 테스트 데이터
        String userId = "user123";
        Long balance = 10000L;

        BalanceResponseDTO expected = BalanceResponseDTO.builder()
                .userId(userId)
                .originalBalance(balance)
                .updatedBalance(balance) // 이 경우 잔액이 변경되지 않았다고 가정
                .build();

        // userRepository의 메소드 모킹
        when(userRepository.checkBalance(userId)).thenReturn(expected);

        // 실제 서비스 메소드 호출 및 결과 검증
        BalanceResponseDTO result = balanceService.checkBalance(userId);
        assertEquals(expected, result);

        // userRepository의 checkBalance 메소드 호출 확인
        verify(userRepository).checkBalance(userId);
    }

}
