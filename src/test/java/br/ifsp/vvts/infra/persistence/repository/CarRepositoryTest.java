package br.ifsp.vvts.infra.persistence.repository;

import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.utils.EntityBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@SpringBootTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    void tearDown() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save a car and find it by license plate")
    void shouldSaveAndFindCarByLicensePlate() {
        // GIVEN
        CarEntity car = EntityBuilder.createRandomCar();
        carRepository.save(car);
        String plate = car.getLicensePlate().getValue();

        // WHEN
        Optional<CarEntity> foundCar = carRepository.findByLicensePlate(plate);

        // THEN
        assertThat(foundCar).isPresent();
        assertThat(foundCar.get().getLicensePlate().getValue()).isEqualTo(plate);
        assertThat(foundCar.get().getModel()).isEqualTo(car.getModel());
    }

    @Test
    @DisplayName("Should return empty when searching for non-existent license plate")
    void shouldReturnEmptyForNonExistentLicensePlate() {
        // WHEN
        Optional<CarEntity> result = carRepository.findByLicensePlate("XYZ9999");

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when saving duplicate license plate")
    void shouldThrowExceptionForDuplicateLicensePlate() {
        // GIVEN
        CarEntity car1 = EntityBuilder.createRandomCar();
        carRepository.save(car1);

        CarEntity car2 = EntityBuilder.createRandomCar();
        car2.setLicensePlate(car1.getLicensePlate());

        // WHEN / THEN
        assertThatThrownBy(() -> carRepository.save(car2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}