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
public class CustomerControllerIntegrationTest extends BaseApiIntegrationTest{

    @Autowired
    private CustomerRepository customerRepository;

    private final String MOCK_CPF = "16396650800";

    @AfterEach
    void tearDown(){
        customerRepository.deleteAll();
    }

    private final String MOCK_PASSWORD = "senhamuitoforte";

    private String setupAuth(){
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
}
