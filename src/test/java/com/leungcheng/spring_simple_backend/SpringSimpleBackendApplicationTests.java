package com.leungcheng.spring_simple_backend;

import static com.leungcheng.spring_simple_backend.domain.order.PurchaseItems.INVALID_QUANTITY_MSG;
import static com.leungcheng.spring_simple_backend.testutil.CustomAssertions.assertBigDecimalEquals;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.leungcheng.spring_simple_backend.domain.Product;
import com.leungcheng.spring_simple_backend.domain.ProductRepository;
import com.leungcheng.spring_simple_backend.domain.User;
import com.leungcheng.spring_simple_backend.domain.UserRepository;
import com.leungcheng.spring_simple_backend.domain.order.OrderService;
import com.leungcheng.spring_simple_backend.validation.ObjectValidator.ObjectValidationException;
import java.math.BigDecimal;
import java.util.stream.Collectors;
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

  private String accessToken = "";

  @BeforeEach
  public void setup() {
    productRepository.deleteAll();
    userRepository.deleteAll();
  }

  private void setAccessToken(String token) {
    this.accessToken = token;
  }

  private void clearAccessToken() {
    this.accessToken = "";
  }

  private boolean isAccessTokenSet() {
    return !this.accessToken.isEmpty();
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
  void shouldCreateAndGetProduct() throws Exception {
    useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    params.price = "19.2";
    MvcResult mvcResult =
        createProduct(params)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(params.name))
            .andExpect(jsonPath("$.price").value("19.2"))
            .andExpect(jsonPath("$.quantity").value(params.quantity))
            .andReturn();
    String productId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.id");

    getProduct(productId)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(params.name))
        .andExpect(jsonPath("$.price").value("19.2"))
        .andExpect(jsonPath("$.quantity").value(params.quantity));
  }

  @Test
  void shouldIgnoreIdWhenCreateProduct() throws Exception {
    useNewUserAccessToken();

    MockHttpServletRequestBuilder request =
        post("/products")
            .contentType("application/json")
            .content(
                "{\"id\": \"should-be-ignored\", \"name\": \"Product 1\", \"price\": 1.0, \"quantity\": 50}");
    addAuthHeader(request);

    mockMvc
        .perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", not("should-be-ignored")));
  }

  @Test
  void shouldHandleObjectValidationException_AndIncludeTheProperInfoInTheResponse()
      throws Exception {
    useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    params.price = "-1";

    // Set up the expected exception that will be thrown when building this product
    ObjectValidationException expectedException = null;
    try {
      new Product.Builder()
          .name(params.name)
          .price(new BigDecimal(params.price))
          .quantity(params.quantity)
          .build();
    } catch (ObjectValidationException ex) {
      expectedException = ex;
    }
    if (expectedException == null) {
      throw new AssertionError("Expected exception not thrown");
    }

    // Assert the expected exception is handled correctly
    createProduct(params)
        .andExpect(status().isBadRequest())
        .andExpect(content().string("price: " + expectedException.getFirstErrorMessage()));
  }

  @Test
  void shouldGet404WhenProductNotFound() throws Exception {
    useNewUserAccessToken();

    getProduct("invalid-id")
        .andExpect(status().isNotFound())
        .andExpect(content().string("Could not find product invalid-id"));
  }

  @Test
  void shouldRejectSignupWithInvalidUsername() throws Exception {
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
  void shouldRejectSignupWithInvalidPassword() throws Exception {
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
  void shouldRejectSignupWhenUsernameExists() throws Exception {
    signup(new UserCredentials("user01", "password")).andExpect(status().isCreated());

    UserCredentials otherUserCredentials = new UserCredentials("user01", "password2");
    signup(otherUserCredentials)
        .andExpect(status().isConflict())
        .andExpect(content().string("Username user01 already exists"));
    login(otherUserCredentials).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectLoginWithIncorrectPassword() throws Exception {
    signup(new UserCredentials("user01", "password")).andExpect(status().isCreated());
    login(new UserCredentials("user01", "password2")).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectLoginWithNonexistentUsername() throws Exception {
    login(new UserCredentials("nonexistentuser", "password")).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectNonAuthApiCallWithoutToken() throws Exception {
    clearAccessToken();
    createProduct(CreateProductParams.sample()).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectIfApiCallWithInvalidToken() throws Exception {
    setAccessToken("invalid-token");
    createProduct(CreateProductParams.sample()).andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectIfAuthHeaderIsNotSetCorrectly() throws Exception {
    useNewUserAccessToken();

    mockMvc
        .perform(
            post("/products")
                .contentType("application/json")
                .header("Authorization", "NotBearer " + accessToken)
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
  void shouldCreateProductWithUserIdSameAsCreator() throws Exception {
    String userId = useNewUserAccessToken();

    CreateProductParams params = CreateProductParams.sample();
    createProduct(params)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(userId));
  }

  @Test
  void shouldGetAccountInfo() throws Exception {
    UserCredentials userCredentials = UserCredentials.sample();
    signup(userCredentials);

    MvcResult result = login(userCredentials).andReturn();
    String token = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
    setAccessToken(token);

    result =
        getAccountInfo()
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(userCredentials.username))
            .andReturn();

    BigDecimal actualBalance =
        BigDecimal.valueOf(
            (Double) JsonPath.read(result.getResponse().getContentAsString(), "$.balance"));
    assertBigDecimalEquals(User.INITIAL_BALANCE, actualBalance);
  }

  @Test
  void shouldCreateOrder() throws Exception {
    String userId = useNewUserAccessToken();

    CreateProductParams productParams = CreateProductParams.sample();

    // Product 1
    productParams.price = "1";
    productParams.quantity = 99;
    MvcResult result = createProduct(productParams).andReturn();
    String product1Id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    // Product 2
    result = createProduct(productParams).andReturn();
    String product2Id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    // Create Order
    CreateOrderParams createOrderParams =
        new CreateOrderParams("request-001", ImmutableMap.of(product1Id, 4, product2Id, 5));

    createOrder(createOrderParams)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.purchaseItems").exists())
        .andExpect(jsonPath("$.purchaseItems.productIdToQuantity." + product1Id).value(4))
        .andExpect(jsonPath("$.purchaseItems.productIdToQuantity." + product2Id).value(5))
        .andExpect(jsonPath("$.requestId").value("request-001"))
        .andExpect(jsonPath("$.buyerUserId").value(userId));
  }

  @Test
  void shouldCreateOrderApiHandleExceptionDueToNegativeQuantity() throws Exception {
    useNewUserAccessToken();

    CreateProductParams productParams = CreateProductParams.sample();
    MvcResult result = createProduct(productParams).andReturn();
    String productId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    CreateOrderParams createOrderParams =
        new CreateOrderParams("request-001", ImmutableMap.of(productId, -1));

    createOrder(createOrderParams)
        .andExpect(status().isBadRequest())
        .andExpect(content().string(INVALID_QUANTITY_MSG));
  }

  @Test
  void shouldCreateOrderApiHandleCreateOrderExceptionFromOrderService() throws Exception {
    useNewUserAccessToken();

    CreateProductParams productParams = CreateProductParams.sample();
    productParams.quantity = 0;
    MvcResult result = createProduct(productParams).andReturn();
    String productId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    CreateOrderParams createOrderParams =
        new CreateOrderParams("request-001", ImmutableMap.of(productId, 1));

    createOrder(createOrderParams)
        .andExpect(status().isBadRequest())
        .andExpect(
            content().string(OrderService.CreateOrderException.insufficientStockMsg(productId)));
  }

  private static class CreateProductParams {
    String name;
    String price;
    int quantity;

    private static CreateProductParams sample() {
      return new CreateProductParams("Product 1", "10", 50);
    }

    private CreateProductParams(String name, String price, int quantity) {
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
    addAuthHeader(builder);
    return mockMvc.perform(builder);
  }

  private ResultActions getProduct(String id) throws Exception {
    MockHttpServletRequestBuilder builder = get("/products/" + id);
    addAuthHeader(builder);
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

  private ResultActions getAccountInfo() throws Exception {
    MockHttpServletRequestBuilder builder = get("/me");
    addAuthHeader(builder);
    return mockMvc.perform(builder);
  }

  private ResultActions createOrder(CreateOrderParams createOrderParams) throws Exception {
    MockHttpServletRequestBuilder builder =
        post("/orders").contentType("application/json").content(createOrderParams.toContent());
    addAuthHeader(builder);
    return mockMvc.perform(builder);
  }

  private static class CreateOrderParams {
    String requestId;
    ImmutableMap<String, Integer> productIdToQuantity;

    CreateOrderParams(String requestId, ImmutableMap<String, Integer> productIdToQuantity) {
      this.requestId = requestId;
      this.productIdToQuantity = productIdToQuantity;
    }

    String toContent() {
      return "{\"requestId\": \""
          + this.requestId
          + "\", \"productIdToQuantity\": "
          + productIdToQuantityContent()
          + "}";
    }

    private String productIdToQuantityContent() {
      return productIdToQuantity.entrySet().stream()
          .map(entry -> "\"" + entry.getKey() + "\": " + entry.getValue())
          .collect(Collectors.joining(", ", "{", "}"));
    }
  }

  private void addAuthHeader(MockHttpServletRequestBuilder builder) {
    if (isAccessTokenSet()) {
      builder.header("Authorization", "Bearer " + this.accessToken);
    }
  }
}
