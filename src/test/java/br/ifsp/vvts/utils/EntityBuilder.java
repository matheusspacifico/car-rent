package br.ifsp.vvts.utils;

import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import com.github.javafaker.Faker;

import java.util.Locale;

public class EntityBuilder {

    private static final Faker faker = new Faker(new Locale("pt-BR"));

    public static CarEntity createRandomCar() {
        String licensePlate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
        return new CarEntity(
                null,
                new LicensePlateEmbeddable(licensePlate),
                faker.company().name(),
                faker.commerce().productName(),
                faker.number().randomDouble(2, 50, 500)
        );
    }
}