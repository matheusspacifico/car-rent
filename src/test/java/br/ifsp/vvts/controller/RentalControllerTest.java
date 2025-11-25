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

        savedCar = new CarEntity();
        savedCar.setLicensePlate(new LicensePlateEmbeddable("ABC-1234"));

        carRepository.save(savedCar);

        savedCustomer = new CustomerEntity(
                null,
                "Costumer Test",
                new CPFEmbeddable("111.111.111.11")
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

}