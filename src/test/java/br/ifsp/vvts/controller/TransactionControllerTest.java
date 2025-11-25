package br.ifsp.vvts.controller;

import br.ifsp.vvts.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@Tag("IntegrationTest")
@Tag("ApiTest")
class TransactionControllerTest extends BaseApiIntegrationTest {

    // Why not? Why wouldn't I test a Hello World controller...?

    @Test
    @DisplayName("Should return Hello message with Authenticated User ID")
    void shouldReturnHelloWithUserId() {
        User user = registerUser("123456");
        String token = authenticate(user.getUsername(), "123456");

        given()
                .header("Authorization", "Bearer " + token)
                .port(port)
                .when()
                .get("/api/v1/hello")
                .then()
                .statusCode(200)
                .body(containsString("Hello: "))
                .body(containsString(user.getId().toString()));
    }
}