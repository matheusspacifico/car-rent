package br.ifsp.vvts.controller;

import br.ifsp.vvts.domain.dto.CreateRentalRequest;
import br.ifsp.vvts.domain.model.rental.RentalStatus;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CPFEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import br.ifsp.vvts.infra.persistence.entity.rental.RentalEntity;
import br.ifsp.vvts.infra.persistence.repository.CarRepository;
import br.ifsp.vvts.infra.persistence.repository.CustomerRepository;
import br.ifsp.vvts.infra.persistence.repository.RentalRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Tag("IntegrationTest")
@Tag("ApiTest")
class RentalControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RentalRepository rentalRepository;

    private String validToken;
    private CarEntity savedCar;
    private CustomerEntity savedCustomer;

    @BeforeEach
    void setup() {
        var user = registerUser("password123");
        this.validToken = authenticate(user.getUsername(), "password123");

        savedCar = new CarEntity(
                null,
                new LicensePlateEmbeddable("ABC1D23"),
                "Toyota",
                "Corolla",
                50000.0
        );

        carRepository.save(savedCar);

        savedCustomer = new CustomerEntity(
                null,
                "Costumer Test",
                new CPFEmbeddable("16396650800")
        );

        customerRepository.save(savedCustomer);
    }

    @AfterEach
    void tearDown() {
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();
        super.tearDown();
    }

    @Test
    @DisplayName("Should create rental successfully and return 201 with Location")
    void shouldCreateRentalSuccessfully() {
        CreateRentalRequest request = new CreateRentalRequest(
                "ABC1D23",
                "16396650800",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                true
        );

        given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/rentals")
                .then()
                .statusCode(201)
                .header("Location", containsString("/api/v1/rentals/"))
                .body("id", notNullValue())
                .body("status", equalTo("ACTIVE"))
                .body("totalPrice", notNullValue());
    }

    @Test
    @DisplayName("Should return 400 when creating rental for non-existent car")
    void shouldFailCreateRentalCarNotFound() {
        CreateRentalRequest request = new CreateRentalRequest(
                "ZZZ-9999",
                "16396650800",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                true
        );

        given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/rentals")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should list all rentals returning 200 OK")
    void shouldListAllRentals() {
        RentalEntity rental = new RentalEntity(null, savedCustomer, savedCar,
                LocalDate.now(), LocalDate.now().plusDays(2), BigDecimal.TEN, RentalStatus.ACTIVE);
        rentalRepository.save(rental);

        given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/rentals")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].car.licensePlate", equalTo("ABC1D23"));
    }

    @Test
    @DisplayName("Should find rental by ID returning 200 OK")
    void shouldFindRentalById() {
        RentalEntity rental = new RentalEntity(null, savedCustomer, savedCar,
                LocalDate.now(), LocalDate.now().plusDays(2), BigDecimal.TEN, RentalStatus.ACTIVE);
        RentalEntity saved = rentalRepository.save(rental);

        given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/rentals/{id}", saved.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(saved.getId().intValue()));
    }

    @Test
    @DisplayName("Should return 404 when finding non-existent rental ID")
    void shouldReturn404ForNonExistentId() {
        given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .get("/api/v1/rentals/{id}", 99999L)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should delete rental and return 204 No Content")
    void shouldDeleteRental() {
        RentalEntity rental = new RentalEntity(null, savedCustomer, savedCar,
                LocalDate.now(), LocalDate.now().plusDays(2), BigDecimal.TEN, RentalStatus.ACTIVE);
        RentalEntity saved = rentalRepository.save(rental);

        given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/rentals/{id}", saved.getId())
                .then()
                .statusCode(204);

        assertThat(rentalRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent rental")
    void shouldReturn404WhenDeletingNonExistent() {
        given()
                .header("Authorization", "Bearer " + validToken)
                .when()
                .delete("/api/v1/rentals/{id}", 99999L)
                .then()
                .statusCode(404);
    }
}