package com.leungcheng.spring_simple_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSimpleBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldCreateAndGetProduct() throws Exception {
	 	MvcResult mvcResult = mockMvc.perform(post("/products")
				.content("{\"name\": \"Product 1\", \"price\": 1.0, \"quantity\": 50}"))
				.andExpect(status().isCreated())
				.andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location))
				.andExpect(status().isOk())
			 	.andExpect(jsonPath("$.name").value("Product 1"))
			 	.andExpect(jsonPath("$.price").value(1.0))
			 	.andExpect(jsonPath("$.quantity").value(50));
	}

	@Test
	public void shouldRejectCreateProductWithInvalidData() throws Exception {
		mockMvc.perform(post("/products")
				.content("{\"name\": \"\", \"price\": 1.0, \"quantity\": 50}"))
				.andExpect(status().isBadRequest());
		mockMvc.perform(post("/products")
				.content("{\"name\": \"Product 1\", \"price\": -1.0, \"quantity\": 50}"))
				.andExpect(status().isBadRequest());
	}
}
