package com.example.healthcheck;

import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.repository.OrderDetailRepository;
import com.example.healthcheck.domain.order.repository.OrderRepository;
import com.example.healthcheck.domain.order.event.DataPlatformCenter;
import com.example.healthcheck.domain.order.service.OrderService;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest
public class ConcurrencyTest {
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
    // 동시에 여러건의 주문이 이러우져도 정상적으로 잔액 차감이 이뤄지는지 테스트
    // 다양한 프로덕트를 동시에 차감했을 때도 원자성이 보장되는지,
    // 여러 스레드에서 동시에 잔액 차감을 시도할 때 음수 잔액이 발생하지 않는지 테스트


    @Test
    public void testConcurrentOrderProcessing() throws Exception {
        String userId = "user123";
        String productId = "product123";
        int numberOfOrders = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfOrders);

        CompletableFuture<Void>[] futures = new CompletableFuture[numberOfOrders];
        for (int i = 0; i < numberOfOrders; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    HashMap<String, Integer> inputData = new HashMap<>();
                    inputData.put(productId, 1);
                    OrderDTO testData = OrderDTO.builder()
                            .userId(userId)
                            .productQuantities(inputData)
                            .build();
                    orderService.placeOrder(testData); // 각 주문은 1개의 상품을 주문
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, executor);
        }

        CompletableFuture.allOf(futures).get();

        assertUserBalanceAndProductStock(userId, productId);

        executor.shutdown();
    }

    @Test
    private void setupInitialData() {
        // 사용자 및 상품 초기 데이터 설정
        HashMap<String, Integer> initialBalance = new HashMap<>();
        initialBalance.put("user123", 10000);
        HashMap<String, Integer> initialProductStock = new HashMap<>();
        initialProductStock.put("product123", 50);
    }

    @Test
    private void assertUserBalanceAndProductStock(String userId, String productCode) {
        // 사용자 잔액 및 상품 재고 검증
        Long userBalance = userRepository.getBalanceByUserId(userId);
        int productStock = productRepository.findStockQuantityByProductCode(productCode);

        assertTrue(userBalance >= 0, "잔액이 음수입니다");
        assertTrue(productStock >= 0, "재고가 음수입니다");
    }

}
