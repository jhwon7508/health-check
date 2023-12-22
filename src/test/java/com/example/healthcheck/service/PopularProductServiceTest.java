package com.example.healthcheck.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class PopularProductServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTopSellingProducts() throws Exception {
        // 최근 3일간 가장 많이 팔린 상위 5개 상품 조회 테스트
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/popular-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);

        // 결과 확인: 최근 3일간 판매된 상위 5개 상품 정보가 정확하게 조회되는지 확인
        // 통계 정보가 올바르게 계산되었는지 확인
        // 이 부분에 실제 상위 상품 조회 로직을 사용하여 데이터베이스 또는 통계 정보가 정확하게 반환되는지 확인하는 코드를 추가
    }
}
