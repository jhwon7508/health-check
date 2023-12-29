package com.example.healthcheck.domain.order.event;

import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.example.healthcheck.domain.order.dto.request.OrderDTO;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class DataPlatformCenter {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public DataPlatformCenter(OrderService orderService, OrderDTO orderDTO) {
    }

    public void processOrderData(OrderDTO orderDTO) {
        Boolean isSuccess = true;
        // 이벤트 발행
        eventPublisher.publishEvent(new DataPlatformProcessEvent(this, isSuccess));
    }
}
