package com.example.healthcheck.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum NamingCode {

    //Product
    PRODUCT_BASE("P-"),

    //Order
    ORDER_BASE("-O-"),

    //Process
    PENDING("주문 대기 중"),
    CONFIRMED("주문 확인"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("주문 취소"),

    //Category
    NOVEL("NOV"),       // 소설
    HUMANITIES("HUM"),  // 인문
    LITERATURE("LIT"),  // 기타문학
    ECONOMY("ECO"),     // 경제/경영
    SCIENCE("SCI"),     // 과학
    CHILDREN("CHI"),    // 어린이
    LANGUAGE("LAN"),    // 외국어
    STUDY("STU"),       // 수험서
    TRAVEL("TRA"),      // 여행
    OTHERS("ETC");      // 기타



    private String code;
}
