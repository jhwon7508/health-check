package com.example.healthcheck.service;

import com.example.healthcheck.domain.dto.request.OrderDTO;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.repository.OrderRepository;
import com.example.healthcheck.restApi.repository.PaymentRepository;
import com.example.healthcheck.restApi.repository.ProductRepository;
import com.example.healthcheck.restApi.repository.UserRepository;
import com.example.healthcheck.restApi.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(userRepository, productRepository, paymentRepository, orderRepository);
    }

    // 주문 금액이 사용자 잔고보다 많아서 실패하는 경우 테스트
    @Test
    void orderFailsWhenBalanceInsufficient() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(userRepository.getCurrentBalance(orderDTO.getUserId())).thenReturn(5000L);
        when(productRepository.findPriceByProductId("product1")).thenReturn(1000L);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_1002.getMessage(), exception.getMessage());
    }

    // 주문 수량이 상품 재고보다 많아서 실패하는 경우 테스트
    @Test
    void orderFailsWhenQuantityExceedsStock() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(productRepository.getStockQuantity("product1")).thenReturn(5);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_2002.getMessage(), exception.getMessage());
    }

    // 주문한 상품이 매진(isSoldOut=true)이라서 실패하는 경우 테스트
    @Test
    void orderFailsWhenProductIsSoldOut() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(productRepository.isProductSoldOut("product1")).thenReturn(true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_2002.getMessage(), exception.getMessage());
    }

    // 외부 결제 시스템에 문제가 생겨서 실패하는 경우 테스트
    @Test
    void orderFailsDueToPaymentSystemError() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(userRepository.getCurrentBalance(orderDTO.getUserId())).thenReturn(10000L);
        when(productRepository.findPriceByProductId("product1")).thenReturn(1000L);
        doThrow(new RuntimeException(ResponseCode.CODE_2005.getMessage())).when(paymentRepository).processPayment(any());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_2005.getMessage(), exception.getMessage());
    }

    // 필수 입력 정보가 누락되어 실패하는 경우 테스트
    @Test
    void orderFailsDueToMissingRequiredInfo() {
        OrderDTO orderDTO = new OrderDTO(null, "user123", "123 테스트 주소", new HashMap<>(), "paymentCode123");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_2004.getMessage(), exception.getMessage());
    }

    // 주문 코드(orderCode)가 중복되는 경우 실패하는 경우 테스트
    @Test
    void orderFailsWhenOrderCodeIsDuplicated() {
        OrderDTO orderDTO = createTestOrderDTO();
        when(userRepository.getCurrentBalance(orderDTO.getUserId())).thenReturn(10000L);
        when(productRepository.findPriceByProductId("product1")).thenReturn(1000L);
        when(orderRepository.existsByOrderCode(anyString())).thenReturn(true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.placeOrder(orderDTO);
        });

        assertEquals(ResponseCode.CODE_2003.getMessage(), exception.getMessage());
    }

    // 테스트용 OrderDTO 객체 생성 도우미 메소드
    private OrderDTO createTestOrderDTO() {
        return new OrderDTO("orderId123", "user123", "123 테스트 주소", new HashMap<>() {{
            put("product1", 10);
        }}, "paymentCode123");
    }
}

