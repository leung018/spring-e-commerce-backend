package com.leungcheng.spring_simple_backend;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.User;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSimpleBackendApplicationTests {

  @Autowired private MockMvc mockMvc;
  @Autowired private ProductRepository productRepository;
  @Autowired private UserRepository userRepository;

  private Optional<String> accessToken = Optional.empty();

  @BeforeEach
  public void setup() {
    productRepository.deleteAll();
    userRepository.deleteAll();
  }

  private void setAccessToken(String token) {
    this.accessToken = Optional.of(token);
  }

  private void clearAccessToken() {
    this.accessToken = Optional.empty();
  }

  private String useNewUserAccessToken() throws Exception {
    UserCredentials userCredentials = UserCredentials.sample();

    signup(userCredentials).andExpect(status().isCreated());
    User user = userRepository.findByUsername(userCredentials.username).orElseThrow();

    MvcResult result = login(userCredentials).andExpect(status().isOk()).andReturn();
    String token = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
    setAccessToken(token);

    return user.getId();
  }

  @Test
  public void shouldCreateAndGetProduct() throws Exception {
    useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    MvcResult mvcResult =
        createProduct(params)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(params.name))
            .andExpect(jsonPath("$.price").value(params.price))
            .andExpect(jsonPath("$.quantity").value(params.quantity))
            .andReturn();
    String productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

    getProduct(productId)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(params.name))
        .andExpect(jsonPath("$.price").value(params.price))
        .andExpect(jsonPath("$.quantity").value(params.quantity));
  }

  @Test
  public void shouldIgnoreIdWhenCreateProduct() throws Exception {
    useNewUserAccessToken();

    mockMvc
        .perform(
            post("/products")
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken.orElseThrow())
                .content(
                    "{\"id\": \"should-be-ignored\", \"name\": \"Product 1\", \"price\": 1.0, \"quantity\": 50}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", not("should-be-ignored")));
  }

  @Test
  public void shouldRejectCreateProductWithInvalidData() throws Exception {
    useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    params.name = "";
    createProduct(params).andExpect(status().isBadRequest());

    params = CreateProductParams.sample();
    params.price = -1;
    createProduct(params).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldGet404WhenProductNotFound() throws Exception {
    useNewUserAccessToken();

    getProduct("invalid-id")
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find product invalid-id"));
  }

  @Test
  public void shouldRejectSignupWithInvalidUsername() throws Exception {
    assertSignupRejectUsername("1".repeat(4));
    assertSignupRejectUsername("1".repeat(21));
    assertSignupRejectUsername("NonLowerCase");
    assertSignupRejectUsername("non-alphanumeric&3");
  }

  private void assertSignupRejectUsername(String username) throws Exception {
    UserCredentials userCredentials = UserCredentials.sample();
    userCredentials.username = username;
    signup(userCredentials).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldRejectSignupWithInvalidPassword() throws Exception {
    assertSignupRejectPassword("1".repeat(7));
    assertSignupRejectPassword("1".repeat(51));
    assertSignupRejectPassword("i have space");
  }

  private void assertSignupRejectPassword(String password) throws Exception {
    UserCredentials userCredentials = UserCredentials.sample();
    userCredentials.password = password;
    signup(userCredentials).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldRejectSignupWhenUsernameExists() throws Exception {
    signup(new UserCredentials("user01", "password")).andExpect(status().isCreated());

    UserCredentials otherUserCredentials = new UserCredentials("user01", "password2");
    signup(otherUserCredentials)
        .andExpect(status().isConflict())
        .andExpect(content().string("Username user01 already exists"));
    login(otherUserCredentials).andExpect(status().isForbidden());
  }

  @Test
  public void shouldRejectLoginWithIncorrectPassword() throws Exception {
    signup(new UserCredentials("user01", "password")).andExpect(status().isCreated());
    login(new UserCredentials("user01", "password2")).andExpect(status().isForbidden());
  }

  @Test
  public void shouldRejectLoginWithNonexistentUsername() throws Exception {
    login(new UserCredentials("nonexistentuser", "password")).andExpect(status().isForbidden());
  }

  @Test
  public void shouldRejectNonAuthApiCallWithoutToken() throws Exception {
    clearAccessToken();
    createProduct(CreateProductParams.sample()).andExpect(status().isForbidden());
  }

  @Test
  public void shouldRejectIfApiCallWithInvalidToken() throws Exception {
    setAccessToken("invalid-token");
    createProduct(CreateProductParams.sample()).andExpect(status().isForbidden());
  }

  @Test
  public void shouldRejectIfAuthHeaderIsNotSetCorrectly() throws Exception {
    useNewUserAccessToken();

    mockMvc
        .perform(
            post("/products")
                .contentType("application/json")
                .header("Authorization", "NotBearer " + accessToken.orElseThrow())
                .content(CreateProductParams.sample().toContent()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            post("/products")
                .contentType("application/json")
                .header("Authorization", "Bearer ")
                .content(CreateProductParams.sample().toContent()))
        .andExpect(status().isForbidden());
  }

  @Test
  public void shouldCreateProductWithUserIdSameAsCreator() throws Exception {
    String userId = useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    createProduct(params)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(userId));
  }

  private static class CreateProductParams {
    String name;
    double price;
    int quantity;

    private static CreateProductParams sample() {
      return new CreateProductParams("Product 1", 1.0, 50);
    }

    private CreateProductParams(String name, double price, int quantity) {
      this.name = name;
      this.price = price;
      this.quantity = quantity;
    }

    public String toContent() {
      return "{\"name\": \""
          + this.name
          + "\", \"price\": "
          + this.price
          + ", \"quantity\": "
          + this.quantity
          + "}";
    }
  }

  private ResultActions createProduct(CreateProductParams params) throws Exception {
    MockHttpServletRequestBuilder builder =
        post("/products").contentType("application/json").content(params.toContent());
    this.accessToken.ifPresent(s -> builder.header("Authorization", "Bearer " + s));
    return mockMvc.perform(builder);
  }

  private ResultActions getProduct(String id) throws Exception {
    MockHttpServletRequestBuilder builder = get("/products/" + id);
    this.accessToken.ifPresent(s -> builder.header("Authorization", "Bearer " + s));
    return mockMvc.perform(builder);
  }

  private static class UserCredentials {
    String username;
    String password;

    private static UserCredentials sample() {
      return new UserCredentials("user001", "sample-password");
    }

    private UserCredentials(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public String toContent() {
      return "{\"username\": \"" + this.username + "\", \"password\": \"" + this.password + "\"}";
    }
  }

  private ResultActions signup(UserCredentials userCredentials) throws Exception {
    return mockMvc.perform(
        post("/signup").contentType("application/json").content(userCredentials.toContent()));
  }

  private ResultActions login(UserCredentials userCredentials) throws Exception {
    return mockMvc.perform(
        post("/login").contentType("application/json").content(userCredentials.toContent()));
  }
}
