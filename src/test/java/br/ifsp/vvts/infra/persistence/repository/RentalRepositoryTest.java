package br.ifsp.vvts.infra.persistence.repository;

import br.ifsp.vvts.domain.model.rental.RentalStatus;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import br.ifsp.vvts.infra.persistence.entity.rental.RentalEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
@Tag("PersistenceTest")
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private CarEntity testCar;
    private CustomerEntity testCustomer;

    @BeforeEach
    void setup() {
        testCar = new CarEntity();
        testCar.setLicensePlate(new LicensePlateEmbeddable("ABC-1234"));

        carRepository.save(testCar);

        testCustomer = new CustomerEntity();

        customerRepository.save(testCustomer);

        RentalEntity testRental = new RentalEntity(
                null,
                testCustomer,
                testCar,
                LocalDate.of(2023, 1, 10),
                LocalDate.of(2023, 1, 20),
                BigDecimal.TEN,
                RentalStatus.ACTIVE
        );

        rentalRepository.save(testRental);
    }

    @AfterEach
    void tearDown() {
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();
    }



}
