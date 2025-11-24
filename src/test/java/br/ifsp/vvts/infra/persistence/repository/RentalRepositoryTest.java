package br.ifsp.vvts.infra.persistence.repository;

import br.ifsp.vvts.domain.model.rental.RentalStatus;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CPFEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import br.ifsp.vvts.infra.persistence.entity.rental.RentalEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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

        testCustomer = new CustomerEntity(
                null,
                "Costumer Test",
                new CPFEmbeddable("111.111.111.11")
        );

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

    @Test
    @DisplayName("Should return true when period exactly matches existing rental")
    void shouldReturnTrueWhenPeriodMatches() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 10),
                LocalDate.of(2023, 1, 20)
        );
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return true when new period is fully inside existing rental")
    void shouldReturnTrueWhenPeriodIsInside() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 12),
                LocalDate.of(2023, 1, 18)
        );
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return true when new period overlaps the end of existing rental")
    void shouldReturnTrueWhenOverlapsEnd() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 15),
                LocalDate.of(2023, 1, 25)
        );
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return true when new period overlaps the start of existing rental")
    void shouldReturnTrueWhenOverlapsStart() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 5),
                LocalDate.of(2023, 1, 15)
        );
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return true when new period encloses existing rental")
    void shouldReturnTrueWhenPeriodEncloses() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 30)
        );
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when period is completely before existing rental")
    void shouldReturnFalseWhenPeriodIsBefore() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 9)
        );
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false when period is completely after existing rental")
    void shouldReturnFalseWhenPeriodIsAfter() {
        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                testCar.getLicensePlate(),
                LocalDate.of(2023, 1, 21),
                LocalDate.of(2023, 1, 30)
        );
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return false when checking for a different car")
    void shouldReturnFalseForDifferentCar() {
        LicensePlateEmbeddable otherPlate = new LicensePlateEmbeddable("XYZ-9999");

        boolean exists = rentalRepository.existsByCarLicensePlateAndPeriodOverlaps(
                otherPlate,
                LocalDate.of(2023, 1, 10),
                LocalDate.of(2023, 1, 20)
        );
        assertThat(exists).isFalse();
    }

}
