package br.ifsp.vvts.controller;

import br.ifsp.vvts.domain.dto.CreateCarRequest;
import br.ifsp.vvts.domain.dto.UpdateCarRequest;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.repository.CarRepository;
import br.ifsp.vvts.security.user.User;
import br.ifsp.vvts.utils.EntityBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Tag("ApiTest")
@Tag("IntegrationTest")
class CarControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    void clearCars() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create car and return 201 when authenticated and data is valid")
    void shouldCreateCarSuccessfully() {
        // GIVEN
        String password = "password123";
        User user = registerUser(password);
        String token = authenticate(user.getUsername(), password);

        CreateCarRequest request = new CreateCarRequest(
                "ABC1D23", "Toyota", "Corolla", 150.00
        );

        // WHEN / THEN
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/cars")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(201)
                .header("Location", containsString("/api/v1/cars/ABC1D23"))
                .body("licensePlate.value", equalTo("ABC1D23"))
                .body("model", equalTo("Corolla"));
    }

    @Test
    @DisplayName("Should return 403 Forbidden when creating car without token")
    void shouldReturnForbiddenWhenNotAuthenticated() {
        CreateCarRequest request = new CreateCarRequest(
                "ABC1D23", "Toyota", "Corolla", 150.00
        );

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/cars")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should list all cars")
    void shouldListAllCars() {
        // GIVEN
        CarEntity car1 = EntityBuilder.createRandomCar();
        CarEntity car2 = EntityBuilder.createRandomCar();
        carRepository.save(car1);
        carRepository.save(car2);

        String password = "123";
        User user = registerUser(password);
        String token = authenticate(user.getUsername(), password);

        // WHEN / THEN
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get("/api/v1/cars")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2))
                .body("licensePlate.value", hasItems(
                        car1.getLicensePlate().getValue(),
                        car2.getLicensePlate().getValue()
                ));
    }

    @Test
    @DisplayName("Should update car details successfully")
    void shouldUpdateCar() {
        // GIVEN
        CarEntity car = EntityBuilder.createRandomCar();
        carRepository.save(car);
        String plate = car.getLicensePlate().getValue();

        String password = "123";
        User user = registerUser(password);
        String token = authenticate(user.getUsername(), password);

        UpdateCarRequest updateRequest = new UpdateCarRequest(
                "Honda", "Civic", 200.00
        );

        // WHEN / THEN
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .put("/api/v1/cars/{plate}", plate)
                .then()
                .statusCode(200)
                .body("brand", equalTo("Honda"))
                .body("model", equalTo("Civic"))
                .body("basePrice", equalTo(200.0f));
    }

    @Test
    @DisplayName("Should delete car successfully")
    void shouldDeleteCar() {
        // GIVEN
        CarEntity car = EntityBuilder.createRandomCar();
        carRepository.save(car);
        String plate = car.getLicensePlate().getValue();

        String password = "123";
        User user = registerUser(password);
        String token = authenticate(user.getUsername(), password);

        // WHEN / THEN
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/v1/cars/{plate}", plate)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/cars/{plate}", plate)
                .then()
                .statusCode(404);
    }
}