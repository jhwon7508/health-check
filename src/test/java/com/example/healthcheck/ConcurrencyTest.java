package com.example.healthcheck;

import com.example.healthcheck.domain.dto.request.OrderDTO;
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

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ConcurrencyTest {
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

    // 여러 스레드에서 동시에 잔액 차감을 시도할 때 음수 잔액이 발생하지 않는지 테스트
    @Test
    void concurrentBalanceDeductionShouldNotResultInNegativeBalance() throws InterruptedException {
        String userId = "user123";
        Long initialBalance = 10000L;
        when(userRepository.getCurrentBalance(userId)).thenReturn(initialBalance);
        when(productRepository.findPriceByProductId(anyString())).thenReturn(1000L);

        int numberOfThreads = 15;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try {
                    OrderDTO orderDTO = new OrderDTO("orderId" + Thread.currentThread().getId(), userId, "123 주소", new HashMap<>() {{
                        put("product1", 1);
                    }}, "paymentCode123");
                    orderService.placeOrder(orderDTO);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        verify(userRepository, atLeastOnce()).deductBalance(eq(userId), anyLong());
        verify(userRepository, never()).deductBalance(eq(userId), lt(0L));
    }

    // 여러 스레드에서 동시에 주문을 시도할 때 상품 재고가 과다하게 차감되지 않는지 테스트
    @Test
    void concurrentOrderPlacementShouldNotResultInOverSelling() throws InterruptedException {
        String productId = "product1";
        int initialStock = 10;
        when(productRepository.getStockQuantity(productId)).thenReturn(initialStock);
        when(productRepository.findPriceByProductId(productId)).thenReturn(1000L);

        int numberOfThreads = 15;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try {
                    OrderDTO orderDTO = new OrderDTO("orderId" + Thread.currentThread().getId(), "user123", "123 주소", new HashMap<>() {{
                        put(productId, 1);
                    }}, "paymentCode123");
                    orderService.placeOrder(orderDTO);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        verify(productRepository, atMost(initialStock)).deductStock(eq(productId), eq(1));
    }
}
