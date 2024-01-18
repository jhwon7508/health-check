package com.example.healthcheck.service;

import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.user.entity.User;
import com.example.healthcheck.domain.user.repository.UserRepository;
import com.example.healthcheck.domain.user.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BalanceServiceTest {

    @Mock
    private UserRepository userRepository;

    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        balanceService = new BalanceService(userRepository);
    }

    // 잔액 충전 시나리오에 대한 테스트
    @Test
    public void testChargeBalance() {
        // 테스트 데이터
        String user = "user123";
        Long amount = 10000L;

        BalanceDTO balanceDto = new BalanceDTO(user, amount);

        // 모킹: UserRepository에서 사용자 조회 시 사용자를 반환
        when(userRepository.findByUserId(user)).thenReturn(new User(user, amount));

        // 실제 서비스 메소드 호출
        BalanceResponseDTO result = balanceService.chargeBalance(balanceDto);

        // 기대값 생성
        BalanceResponseDTO expected = new BalanceResponseDTO(user, amount, amount + amount);

        // 결과 검증
        assertEquals(expected, result);
        verify(userRepository, times(1)).findByUserId(user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // 잔액 조회 시나리오에 대한 테스트
    @Test
    public void testCheckBalance() {
        // 테스트 데이터
        String userId = "user123";
        Long balance = 10000L;

        // 모킹: UserRepository에서 사용자 조회 시 사용자를 반환
        when(userRepository.findByUserId(userId)).thenReturn(new User(userId, balance));

        // 실제 서비스 메소드 호출
        BalanceResponseDTO result = balanceService.checkBalance(userId);

        // 기대값 생성
        BalanceResponseDTO expected = new BalanceResponseDTO(userId, balance, balance);

        // 결과 검증
        assertEquals(expected, result);
        verify(userRepository, times(1)).findByUserId(userId);
    }

    // 사용자가 없을 때 잔액 조회 시나리오에 대한 테스트
    @Test
    public void testCheckBalanceUserNotFound() {
        // 테스트 데이터
        String userId = "user123";

        // 모킹: UserRepository에서 사용자 조회 시 null을 반환 (사용자가 없음)
        when(userRepository.findByUserId(userId)).thenReturn(null);

        // 예외가 발생하는지 검증
        assertThrows(ResponseStatusException.class, () -> {
            balanceService.checkBalance(userId);
        });

        verify(userRepository, times(1)).findByUserId(userId);
    }

    // 잔액 충전 시 사용자가 없을 때 예외가 발생하는 시나리오에 대한 테스트
    @Test
    public void testChargeBalanceUserNotFound() {
        // 테스트 데이터
        String user = "user123";
        Long amount = 10000L;

        BalanceDTO balanceDto = new BalanceDTO(user, amount);

        // 모킹: UserRepository에서 사용자 조회 시 null을 반환 (사용자가 없음)
        when(userRepository.findByUserId(user)).thenReturn(null);

        // 예외가 발생하는지 검증
        assertThrows(ResponseStatusException.class, () -> {
            balanceService.chargeBalance(balanceDto);
        });

        verify(userRepository, times(1)).findByUserId(user);
    }
}
