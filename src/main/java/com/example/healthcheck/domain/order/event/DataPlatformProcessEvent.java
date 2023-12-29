package com.example.healthcheck.domain.order.event;

import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class DataPlatformProcessEvent extends ApplicationEvent {

    private final Boolean isSuccessful;

    public DataPlatformProcessEvent(Object source, Boolean isSuccessful) {
        super(source);
        this.isSuccessful = isSuccessful;
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }
}
