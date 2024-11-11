package com.leungcheng.spring_simple_backend;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSimpleBackendApplicationTests {

  @Autowired private MockMvc mockMvc;
  @Autowired private ProductRepository productRepository;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    productRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void shouldCreateAndGetProduct() throws Exception {
    CreateProductParams params = validParams();
    MvcResult mvcResult =
        createProduct(params)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(params.name))
            .andExpect(jsonPath("$.price").value(params.price))
            .andExpect(jsonPath("$.quantity").value(params.quantity))
            .andReturn();
    String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

    mockMvc
        .perform(get("/products/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(params.name))
        .andExpect(jsonPath("$.price").value(params.price))
        .andExpect(jsonPath("$.quantity").value(params.quantity));
  }

  @Test
  public void shouldIgnoreIdWhenCreateProduct() throws Exception {
    mockMvc
        .perform(
            post("/products")
                .contentType("application/json")
                .content(
                    "{\"id\": \"should-be-ignored\", \"name\": \"Product 1\", \"price\": 1.0, \"quantity\": 50}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", not("should-be-ignored")));
  }

  @Test
  public void shouldRejectCreateProductWithInvalidData() throws Exception {
    CreateProductParams params = validParams();
    params.name = "";
    createProduct(params).andExpect(status().isBadRequest());

    params = validParams();
    params.price = -1;
    createProduct(params).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldGet404WhenProductNotFound() throws Exception {
    mockMvc
        .perform(get("/products/invalid-id"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find product invalid-id"));
  }

  @Test
  public void shouldSignupAndLogin() throws Exception {
    mockMvc
        .perform(
            post("/signup")
                .contentType("application/json")
                .content("{\"username\": \"user\", \"password\": \"password\"}"))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/login")
                .contentType("application/json")
                .content("{\"username\": \"user\", \"password\": \"password\"}"))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldRejectLoginWithIncorrectPassword() throws Exception {
    mockMvc
        .perform(
            post("/signup")
                .contentType("application/json")
                .content("{\"username\": \"user\", \"password\": \"password\"}"))
        .andExpect(status().isCreated());
    mockMvc
        .perform(
            post("/login")
                .contentType("application/json")
                .content("{\"username\": \"user\", \"password\": \"invalid-password\"}"))
        .andExpect(status().isForbidden());
  }

  private static class CreateProductParams {
    String name;
    double price;
    int quantity;
  }

  private CreateProductParams validParams() {
    CreateProductParams params = new CreateProductParams();
    params.name = "Product 1";
    params.price = 1.0;
    params.quantity = 50;
    return params;
  }

  private ResultActions createProduct(CreateProductParams params) throws Exception {
    return mockMvc.perform(
        post("/products")
            .contentType("application/json")
            .content(
                "{\"name\": \""
                    + params.name
                    + "\", \"price\": "
                    + params.price
                    + ", \"quantity\": "
                    + params.quantity
                    + "}"));
  }
}
