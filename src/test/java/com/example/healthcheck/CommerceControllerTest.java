package com.example.healthcheck;

import com.example.healthcheck.domain.common.controller.CommerceController;
import com.example.healthcheck.domain.order.dto.request.OrderDTO;
import com.example.healthcheck.domain.order.service.OrderService;
import com.example.healthcheck.domain.product.dto.response.ProductResponseDTO;
import com.example.healthcheck.domain.product.service.ProductService;
import com.example.healthcheck.domain.user.dto.request.BalanceDTO;
import com.example.healthcheck.domain.user.dto.response.BalanceResponseDTO;
import com.example.healthcheck.domain.user.service.BalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommerceController.class)
public class CommerceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BalanceService balanceService;
	@MockBean
	private OrderService orderService;
	@MockBean
	private ProductService productService;

	@Test
	public void testAddBalanceWithValidData() throws Exception {
		// 가상의 BalanceDTO 데이터 생성
		String balanceJson = "{\"userId\":\"user123\", \"balance\":10000}";

		BalanceResponseDTO mockResponse = new BalanceResponseDTO(/* 초기화 파라미터 */);
		when(balanceService.chargeBalance(any(BalanceDTO.class))).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/balance/add")
						.contentType("application/json")
						.content(balanceJson))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("user123"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedBalance").isNumber());
	}

	@Test
	public void testGetBalance() throws Exception {
		String userId = "user123";

		BalanceResponseDTO mockResponse = new BalanceResponseDTO(/* 초기화 파라미터 */);
		when(balanceService.checkBalance(userId)).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/balance")
						.param("userId", userId))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId));
	}

	@Test
	public void testGetProductById() throws Exception {
		Long productIdx = 123L;

		ProductResponseDTO mockProduct = new ProductResponseDTO(/* 초기화 파라미터 */);
		when(productService.getProductById(productIdx)).thenReturn(mockProduct);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/detail/" + productIdx))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productIdx));
		// 추가적인 상품 정보 검증
	}

	@Test
	public void testPlaceOrder() throws Exception {
		String userId = "user123";
		Map<String, Integer> products = new HashMap<>();
		products.put("product1", 2);
		products.put("product2", 3);

		OrderDTO mockOrder = new OrderDTO(/* 초기화 파라미터 */);
//		when(orderService.placeOrder(eq(userId), any(Map.class))).thenReturn(mockOrder);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/request")
						.param("userId", userId)
						.contentType("application/json")
						.content(new ObjectMapper().writeValueAsString(products)))
				.andExpect(status().isOk());
		// 주문 결과에 대한 추가적인 검증
	}

	@Test
	public void testProcessPayment() throws Exception {
		String orderId = "order123";

		// 결제 로직에 대한 응답 모킹
		// 예: when(orderService.processPayment(orderId)).thenReturn(...);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/" + orderId + "/payment"))
				.andExpect(status().isOk());
		// 결제 처리 결과에 대한 추가적인 검증
	}

	@Test
	public void testGetPopularProducts() throws Exception {
		// 인기 상품 로직에 대한 응답 모킹
		// 예: when(productService.getPopularProducts()).thenReturn(...);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/statistcs/popular-products"))
				.andExpect(status().isOk());
		// 인기 상품 조회 결과에 대한 추가적인 검증
	}


}
