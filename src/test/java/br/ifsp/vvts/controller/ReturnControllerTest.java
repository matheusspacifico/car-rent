package br.ifsp.vvts.controller;

import br.ifsp.vvts.domain.dto.ReturnCarRequest;
import br.ifsp.vvts.domain.model.rental.RentalStatus;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CPFEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import br.ifsp.vvts.infra.persistence.entity.rental.RentalEntity;
import br.ifsp.vvts.infra.persistence.repository.CarRepository;
import br.ifsp.vvts.infra.persistence.repository.CustomerRepository;
import br.ifsp.vvts.infra.persistence.repository.RentalRepository;
import br.ifsp.vvts.security.user.User;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Tag("IntegrationTest")
@Tag("ApiTest")
class ReturnControllerTest extends BaseApiIntegrationTest {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private String validToken;
    private CarEntity savedCar;
    private RentalEntity activeRental;
    private CustomerEntity savedCustomer;

    @BeforeEach
    void setup() {
        User user = registerUser("password123");
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
                new CPFEmbeddable("123.456.789-09")
        );

        customerRepository.save(savedCustomer);

        activeRental = new RentalEntity(
                null,
                savedCustomer,
                savedCar,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(2),
                BigDecimal.valueOf(500.00),
                RentalStatus.ACTIVE
        );
        activeRental = rentalRepository.save(activeRental);
    }

    @AfterEach
    void tearDown() {
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();
        super.tearDown();
    }

    @Test
    @DisplayName("Should return car successfully and update rental status")
    void shouldReturnCarSuccessfully() {
        ReturnCarRequest request = new ReturnCarRequest(
                activeRental.getId(),
                LocalDate.now(),
                false,
                false
        );

        given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .port(port)
                .body(request)
                .when()
                .post("/api/v1/returns")
                .then()
                .statusCode(200)
                .body("status", equalTo("FINISHED"))
                .body("finalPrice", notNullValue())
                .body("actualReturnDate", notNullValue());

        RentalEntity updatedRental = rentalRepository.findById(activeRental.getId()).orElseThrow();

        assertThat(updatedRental.getStatus()).isEqualTo(RentalStatus.FINISHED);
        assertThat(updatedRental.getActualReturnDate()).isNotNull();
        assertThat(updatedRental.getFinalPrice()).isNotNull();
    }

    @Test
    @DisplayName("Should return 403 when trying to return an already returned car")
    void shouldReturnErrorForAlreadyReturnedCar() {
        activeRental.setStatus(RentalStatus.FINISHED);
        rentalRepository.save(activeRental);

        ReturnCarRequest request = new ReturnCarRequest(
                activeRental.getId(),
                LocalDate.now(),
                false,
                false
        );

        given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .port(port)
                .body(request)
                .when()
                .post("/api/v1/returns")
                .then()
                .statusCode(403);
    }
}