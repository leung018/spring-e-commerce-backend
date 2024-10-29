package com.leungcheng.spring_simple_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSimpleBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldCreateProduct() throws Exception {
	 	mockMvc.perform(post("/products")
				.content("{\"name\": \"Product 1\", \"price\": 1.0, \"quantity\": 50}"))
				.andExpect(status().isCreated());
	}
}
