package com.example.healthcheck.service;

import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.repository.OrderDetailRepository;
import com.example.healthcheck.domain.order.repository.OrderRepository;
import com.example.healthcheck.domain.order.event.DataPlatformCenter;
import com.example.healthcheck.domain.order.service.OrderService;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.user.repository.UserRepository;
import com.example.healthcheck.domain.common.enums.ResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ApplicationEventPublisher dataPlatformCenter;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(userRepository, productRepository, orderRepository, orderDetailRepository, dataPlatformCenter);
    }

    // 주문 금액이 사용자 잔고보다 많아서 실패하는 경우 테스트
    @Test
    void orderFailsWhenBalanceInsufficient() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(userRepository.getBalanceByUserId(orderDTO.getUserId())).thenReturn(5000L);
        when(productRepository.getById(123L).getPrice()).thenReturn(1000L);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.INSUFFICIENT_BALANCE.getMessage(), exception.getMessage());
    }

    // 주문 수량이 상품 재고보다 많아서 실패하는 경우 테스트
    @Test
    void orderFailsWhenQuantityExceedsStock() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(productRepository.getById(123L).getStockQuantity()).thenReturn(5);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.OUT_OF_STOCK.getMessage(), exception.getMessage());
    }

    // 주문한 상품이 매진(isSoldOut=true)이라서 실패하는 경우 테스트
    @Test
    void orderFailsWhenProductIsSoldOut() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(productRepository.getById(123L).getIsSoldOut()).thenReturn(true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });
        assertEquals(ResponseCode.OUT_OF_STOCK.getMessage(), exception.getMessage());
    }


    // 필수 입력 정보가 누락되어 실패하는 경우 테스트
    @Test
    void orderFailsDueToMissingRequiredInfo() {
        OrderDTO orderDTO = new OrderDTO(null, "user123", "123 테스트 주소", new HashMap<>(), "paymentCode123");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.MISSING_INFO.getMessage(), exception.getMessage());
    }

    // 주문 코드(orderCode)가 중복되는 경우 실패하는 경우 테스트
    @Test
    void orderFailsWhenOrderCodeIsDuplicated() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(userRepository.getBalanceByUserId(orderDTO.getUserId())).thenReturn(10000L);
        when(productRepository.getById(123L).getPrice()).thenReturn(1000L);
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.DUPLICATE_ORDER.getMessage(), exception.getMessage());
    }

    // 데이터 플랫폼 센터 전송에 실패하는 경우 테스트
    @Test
    void orderFailsDueToDataPlatformCenterError() {
    }

    // 테스트용 OrderDTO 객체 생성 도우미 메소드
    private OrderDTO createTestOrderDTO() {
        return new OrderDTO("orderId123", "user123", "123 테스트 주소", new HashMap<>() {{
            put("product1", 10);
        }}, "paymentCode123");
    }
}

