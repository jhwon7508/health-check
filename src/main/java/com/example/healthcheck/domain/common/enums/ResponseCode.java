package com.example.healthcheck.domain.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseCode {
    //common
    SYSTEM_ERROR("시스템 에러"),
    INVALID_USER("유효하지 않는 사용자입니다"),
    DATABASE_ERROR("데이터베이스 오류가 발생했습니다"),

    //order
    INVALID_ORDER("유효하지 않는 주문입니다"),
    INSUFFICIENT_BALANCE("잔액이 부족합니다"),

    //product
    INVALID_PRODUCT("유효하지 않는 상품입니다"),
    OUT_OF_STOCK("품절된 상품입니다"),
    DUPLICATE_ORDER("주문 코드가 중복되었습니다"),
    MISSING_INFO("필수 입력 정보가 누락되었습니다"),
    PAYMENT_ERROR("외부 결제 시스템 오류");

    private String message;
}

