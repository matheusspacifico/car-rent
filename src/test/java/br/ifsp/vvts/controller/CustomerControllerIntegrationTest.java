package br.ifsp.vvts.controller;

import br.ifsp.vvts.domain.dto.CreateCustomerRequest;
import br.ifsp.vvts.infra.persistence.repository.CustomerRepository;
import br.ifsp.vvts.security.user.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("Integration")
public class CustomerControllerIntegrationTest extends BaseApiIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    private final String MOCK_CPF = "16396650800";
    private final String MOCK_CPF2 = "43355468584";

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    private final String MOCK_PASSWORD = "senhamuitoforte";

    private String setupAuth() {
        User user = registerUser(MOCK_PASSWORD);
        return authenticate(user.getEmail(), MOCK_PASSWORD);
    }

    @Test
    @DisplayName("Should return 201 when create customer")
    void shouldReturn201WhenCreateCustomer() {
        String token = setupAuth();
        CreateCustomerRequest request = new CreateCustomerRequest("John JavaScript", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("John JavaScript"));
    }

    @Test
    @DisplayName("Should return 403 or 401 when creating customer without token")
    void shouldReturn403WhenCreatingCustomerWithoutToken() {
        CreateCustomerRequest request = new CreateCustomerRequest("Vasco da Gama", MOCK_CPF);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(is(oneOf(401, 403)));
    }

    @Test
    @DisplayName("Should return 200 and list all customers")
    void shouldReturn200AndListAllCustomers() {
        String token = setupAuth();

        CreateCustomerRequest cust1 = new CreateCustomerRequest("Alfa", MOCK_CPF);
        CreateCustomerRequest cust2 = new CreateCustomerRequest("Betinha", MOCK_CPF2);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(cust1)
                .when().post("/api/v1/customers")
                .then().statusCode(201);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(cust2)
                .when().post("/api/v1/customers")
                .then().statusCode(201);

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/v1/customers")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("name", hasItems("Alfa", "Betinha"));
    }

    @Test
    @DisplayName("Shoudl return 401 or 403 when listing customers token is missing")
    void shouldReturn403WhenListingCustomersTokenMissing() {
        given()
                .when().get("/api/v1/customers")
                .then()
                .statusCode(is(oneOf(401, 403)));
    }

    @Test
    @DisplayName("Should return 200 when found customer using CPF id")
    void shouldReturn200WhenFoundCustomerUsingCPFId() {
        String token = setupAuth();

        CreateCustomerRequest request = new CreateCustomerRequest("Agostinho Carrara", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("Agostinho Carrara"));

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/v1/customers/{cpf}", MOCK_CPF)
                .then()
                .statusCode(200)
                .body("name", equalTo("Agostinho Carrara"));
    }

    @Test
    @DisplayName("Should return 404 when customer is not found by CPF")
    void shouldReturn404WhenCustomerIsNotFoundByCpf() {
        String token = setupAuth();

        given().header("Authorization", "Bearer " + token)
                .when().get("/api/v1/customers/{cpf}", "98765432100")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 401 or 403 when find customer without token")
    void shouldReturn403WhenFindCustomerWithoutToken() {
        given()
                .when().get("/api/v1/customers/{cpf}", "11122000000")
                .then()
                .statusCode(is(oneOf(401, 403)));
    }

    @Test
    @DisplayName("Should return 200 when update customer information")
    void shouldReturn200WhenUpdateCustomerInformation() {
        String token = setupAuth();

        CreateCustomerRequest request = new CreateCustomerRequest("Betinha da silva", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("Betinha da silva"));

        CreateCustomerRequest request2 = new CreateCustomerRequest("John JavaScript", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request2)
                .when().put("/api/v1/customers/{cpf}", MOCK_CPF)
                .then()
                .statusCode(200)
                .body("name", equalTo("John JavaScript"));
    }

    @Test
    @DisplayName("Should return 204 when delete customer")
    void shouldReturn204WhenDeleteCustomer() {
        String token = setupAuth();

        CreateCustomerRequest request = new CreateCustomerRequest("Betinha da silva", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("Betinha da silva"));

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().delete("/api/v1/customers/{cpf}", MOCK_CPF)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Should return 404 when delete customer")
    void shouldReturn404WhenDeleteCustomer() {
        String token = setupAuth();

        CreateCustomerRequest request = new CreateCustomerRequest("Betinha da silva", MOCK_CPF);

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/customers")
                .then()
                .statusCode(201)
                .body("name", equalTo("Betinha da silva"));

        given().header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().delete("/api/v1/customers/{cpf}", MOCK_CPF2)
                .then()
                .statusCode(404);
    }
}
