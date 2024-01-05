package com.example.healthcheck.domain.order.service;

import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.dto.request.OrderInsertDTO;
import com.example.healthcheck.domain.order.dto.response.OrderResponseDTO;
import com.example.healthcheck.domain.order.entity.OrderDetail;
import com.example.healthcheck.domain.order.entity.Orders;
import com.example.healthcheck.domain.order.event.DataPlatformCenter;
import com.example.healthcheck.domain.order.event.DataPlatformProcessEvent;
import com.example.healthcheck.domain.order.repository.OrderDetailRepository;
import com.example.healthcheck.domain.order.repository.OrderRepository;
import com.example.healthcheck.domain.product.entity.Product;
import com.example.healthcheck.domain.product.repository.ProductRepository;
import com.example.healthcheck.domain.user.entity.User;
import com.example.healthcheck.domain.user.repository.UserRepository;
import com.example.healthcheck.domain.common.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.healthcheck.domain.common.enums.NamingCode.ORDER_BASE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements ApplicationListener<DataPlatformProcessEvent> {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ApplicationEventPublisher eventPublisher;

    private volatile Boolean transmissionSuccessYn;

    @Override
    public void onApplicationEvent(DataPlatformProcessEvent event) {
        transmissionSuccessYn = event.isSuccessful();
    }

    @Transactional
    public OrderResponseDTO placeOrder(OrderDTO orderDTO) throws InterruptedException, ExecutionException {
        // 재고 확인
        List<Product> productsToDeduct = checkStock(orderDTO);

        // 재고 부족하지 않은 경우 상품 재고 차감
        if (productsToDeduct.isEmpty()) {
            for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
                String productCode = entry.getKey();
                Integer orderedQuantity = entry.getValue();

                // 상품의 현재 재고 수량을 차감합니다.
                Product product = productRepository.findByProductCode(productCode);
                Integer currentStock = product.getStockQuantity();
                product.setStockQuantity(currentStock - orderedQuantity);
            }
        }

        // 사용자 잔액 확인 및 주문 총액 계산
        User user = userRepository.findByUserId(orderDTO.getUserId());
        Long userBalance = user.getBalance();
        Long orderTotal = calculateOrderTotal(orderDTO);

        // 잔액 부족 시 예외 처리
        if (userBalance < orderTotal)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseCode.CODE_1002.getMessage());

        // 잔액 차감 및 주문 기록
        Long newBalance = deductBalance(user, orderTotal);
        String orderId = recordOrder(orderDTO);

        // 데이터 플랫폼에 주문 정보 전송
        CompletableFuture<Boolean> transmissionResult = sendOrderDataAsync(orderDTO);

        // 응답 객체 생성
        return buildOrderResponse(orderDTO, orderId, newBalance, transmissionResult.get());
    }

    private Long deductBalance(User user, Long orderTotal) {
        Long newBalance = user.getBalance() - orderTotal;
        user.setBalance(newBalance);
        userRepository.save(user);
        return newBalance;
    }

    private CompletableFuture<Boolean> sendOrderDataAsync(OrderDTO orderDTO) {
        return CompletableFuture.supplyAsync(() -> {
            eventPublisher.publishEvent(new DataPlatformCenter(this, orderDTO));
            return transmissionSuccessYn; // 여기서 transmissionSuccessYn는 이벤트 처리 결과를 나타내는 방식으로 설정
        });
    }


    public Long calculateOrderTotal(OrderDTO orderDTO) {
        Long total = 0L;

        for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            String productCode = entry.getKey(); // 상품 ID
            Integer quantity = entry.getValue(); // 주문 수량

            Long price = productRepository.findPriceByProductCode(productCode); // 상품별 가격 조회
            total += price * quantity; // 상품별 총액을 전체 주문 총액에 더하기
        }

        return total; // 계산된 주문 총액 반환
    }

    public String recordOrder(OrderDTO orderDTO) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        User user = userRepository.findByUserId(orderDTO.getUserId());
        String orderCode = timestamp + ORDER_BASE + user.getIdx();

        OrderInsertDTO orderInsertDTO = OrderInsertDTO.builder()
                .user(user)
                .orderCode(orderCode)
                .shippingAddress(orderDTO.getShippingAddress())
                .productQuantities(orderDTO.getProductQuantities())
                .build();

        Orders newOrder = orderInsertDTO.convertToEntity(user);
        orderRepository.save(newOrder);

        return orderCode;
    }

    public List<Product> checkStock(OrderDTO orderDTO) {
        List<Product> sufficientStockProducts = new ArrayList<>();

        // OrderDTO에서 주문한 상품 목록을 가져옵니다.
        HashMap<String, Integer> productQuantities = orderDTO.getProductQuantities();

        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            String productCode = entry.getKey();
            Integer orderedQuantity = entry.getValue();

            // 주문한 상품의 재고 수량을 조회합니다.
            Product product = productRepository.findByProductCode(productCode);

            if (product == null) {
                // 상품이 존재하지 않는 경우 예외 처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_2001.getMessage());
            }

            // 상품의 현재 재고 수량을 가져옵니다.
            int currentStock = product.getStockQuantity();

            // 주문 수량이 재고 수량보다 많거나 같은지 확인합니다.
            if (currentStock < orderedQuantity) {
                // 재고가 부족한 경우 예외 처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.CODE_2002.getMessage());
            }
            sufficientStockProducts.add(product);
        }

        return sufficientStockProducts;
    }

    private void createAndSaveOrderDetails(String orderId, OrderDTO orderDTO) {
        for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            String productCode = entry.getKey();
            Integer orderedQuantity = entry.getValue();

            // 상품 조회
            Product product = productRepository.findByProductCode(productCode);

            // OrderDetail 생성
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrders(orderRepository.findByOrderId(orderId));
            orderDetail.setProduct(product);
            orderDetail.setOrderDetailCode(orderId + "-" + orderDTO.getProductQuantities().keySet());
            orderDetail.setOrderQuantity(orderedQuantity);
            orderDetail.setOrderPrice(product.getPrice() * orderedQuantity);
            orderDetail.setDeleteYn(false);

            // OrderDetail 저장
            orderDetailRepository.save(orderDetail);
        }
    }

    public OrderResponseDTO buildOrderResponse(OrderDTO orderDTO, String orderCode, Long remainingBalance, Boolean transmissionSuccessYn) {
        User user = userRepository.findByUserId(orderDTO.getUserId());
        String userName = user.getUserName();
        LocalDateTime orderTime = orderRepository.findByOrderCode(orderCode).getCreatedAt();

        Map<String, Long> productPrices = new HashMap<>();
        Long totalPrice = 0L;

        for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            String productCode = entry.getKey();
            Integer quantity = entry.getValue();
            Long price = productRepository.findPriceByProductCode(productCode);
            productPrices.put(productCode, price);
            totalPrice += price * quantity;
        }

        return OrderResponseDTO.builder()
                .orderCode(orderCode)
                .orderTime(orderTime)
                .userName(userName)
                .balance(remainingBalance)
                .shippingAddress(orderDTO.getShippingAddress())
                .productQuantities(orderDTO.getProductQuantities())
                .productPrices(productPrices)
                .totalPrice(totalPrice)
                .transmissionSuccessYn(transmissionSuccessYn) // 데이터 플랫폼 전송 성공 여부
                .build();
    }

}

