package com.example.healthcheck.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseCode {
    //common
    CODE_0000("0000", "시스템 에러"),
    CODE_0001("0001", "유효하지 않는 사용자입니다"),
    CODE_0002("0002", "데이터베이스 오류가 발생했습니다"),

    //order
    CODE_1001("1001", "유효하지 않는 주문입니다"),
    CODE_1002("1002", "잔액이 부족합니다"),

    //product
    CODE_2001("2001", "유효하지 않는 상품입니다"),
    CODE_2002("2002", "품절된 상품입니다"),
    CODE_2003("2003", "주문 코드가 중복되었습니다"),
    CODE_2004("2004", "필수 입력 정보가 누락되었습니다"),
    CODE_2005("2005", "외부 결제 시스템 오류");

    private String code;
    private String message;
}

