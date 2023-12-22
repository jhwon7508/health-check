package com.example.healthcheck.restApi.service;

import com.example.healthcheck.domain.dto.request.OrderDTO;
import com.example.healthcheck.domain.dto.request.OrderInsertDTO;
import com.example.healthcheck.domain.dto.response.OrderResponseDTO;
import com.example.healthcheck.domain.entity.Orders;
import com.example.healthcheck.domain.entity.User;
import com.example.healthcheck.enums.ResponseCode;
import com.example.healthcheck.restApi.repository.OrderRepository;
import com.example.healthcheck.restApi.repository.PaymentRepository;
import com.example.healthcheck.restApi.repository.ProductRepository;
import com.example.healthcheck.restApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public OrderResponseDTO placeOrder(OrderDTO orderDTO) {
        // 사용자 잔액 확인
        Long userBalance = userRepository.getCurrentBalance(orderDTO.getUserId());
        // 주문 총액 계산
        Long orderTotal = calculateOrderTotal(orderDTO);

        // 잔액 부족 시 예외 처리
        if (userBalance < orderTotal)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseCode.CODE_1002.getMessage());

        // 잔액 차감 및 주문 기록
        userRepository.deductBalance(orderDTO.getUserId(), orderTotal);
        String orderId = recordOrder(orderDTO);

        // 데이터 플랫폼에 주문 정보 전송
        paymentRepository.sendOrderData(orderDTO);

        // 응답 객체 생성
        return buildOrderResponse(orderDTO, orderId, userBalance - orderTotal);
    }

    private Long calculateOrderTotal(OrderDTO orderDTO) {
        Long total = 0L;

        for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            String productId = entry.getKey(); // 상품 ID
            Integer quantity = entry.getValue(); // 주문 수량

            Long price = productRepository.findPriceByProductId(productId); // 상품별 가격 조회
            total += price * quantity; // 상품별 총액을 전체 주문 총액에 더하기
        }

        return total; // 계산된 주문 총액 반환
    }

    private String recordOrder(OrderDTO orderDTO) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        User user = userRepository.getUserByUserId(orderDTO.getUserId());
        String orderCode = timestamp + "-" + user.getIdx();

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


    private OrderResponseDTO buildOrderResponse(OrderDTO orderDTO, String orderId, Long remainingBalance) {
        User user = userRepository.getUserByUserId(orderDTO.getUserId());
        String userName = user.getUserName();
        LocalDateTime orderTime = LocalDateTime.now();

        Map<String, Long> productPrices = new HashMap<>();
        Long totalPrice = 0L;

        for (Map.Entry<String, Integer> entry : orderDTO.getProductQuantities().entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();
            Long price = productRepository.findPriceByProductId(productId);
            productPrices.put(productId, price);
            totalPrice += price * quantity;
        }

        return OrderResponseDTO.builder()
                .orderId(orderId)
                .orderTime(orderTime)
                .userName(userName)
                .balance(remainingBalance)
                .shippingAddress(orderDTO.getShippingAddress())
                .productQuantities(orderDTO.getProductQuantities())
                .productPrices(productPrices)
                .totalPrice(totalPrice)
                .paymentSuccessYn(true) // 결제 성공 여부
                .build();
    }

}

