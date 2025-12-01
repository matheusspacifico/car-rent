package br.ifsp.vvts.ui;

import br.ifsp.vvts.infra.persistence.repository.CarRepository;
import br.ifsp.vvts.ui.pages.CarFormPage;
import br.ifsp.vvts.ui.pages.CarsPage;
import br.ifsp.vvts.ui.pages.DeleteCarModal;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class CarsPageTest extends AuthenticatedBaseUiTest {

    @Autowired
    private CarRepository carsRepository;

    private CarsPage carsPage;

    @BeforeEach
    public void navigateToCarsPage() {
        navigateTo("/cars");
        carsPage = new CarsPage(driver);
    }

    @AfterEach
    public void tearDown() {
        carsRepository.deleteAll();
    }

    @Nested
    @DisplayName("Page Load Tests")
    class PageLoadTests {
        @Test
        @DisplayName("Should display correct page title")
        public void shouldDisplayTitle() {
            assertThat(carsPage.getPageTitle()).isEqualTo("Carros");
        }

        @Test
        @DisplayName("Should display Add button")
        public void shouldDisplayAddButton() {
            assertThat(carsPage.isAddCarButtonVisible()).isTrue();
        }
    }

    @Nested
    @DisplayName("Car Operations Tests")
    class OperationsTests {

        @Test
        @DisplayName("Should create a new car successfully")
        public void shouldCreateCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            String brand = "Toyota";
            String model = "supra mk5";
            String price = "120.00";

            CarFormPage form = carsPage.clickAddCarButton();
            form.fillForm(plate, brand, model, price);
            carsPage = form.clickSubmit();

            assertThat(carsPage.isCarListed(plate)).isTrue();
            assertThat(carsPage.checkIntegrationCarRowData(plate, brand, model, price)).isTrue();
        }

        @Test
        @DisplayName("Should cancel process to create a new car successfully")
        public void shouldCancelCreateCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            String brand = "Toyota";
            String model = "supra mk5";
            String price = "120.00";

            CarFormPage form = carsPage.clickAddCarButton();
            form.fillForm(plate, brand, model, price);
            carsPage = form.clickCancel();

            assertThat(carsPage.isCarListed(plate)).isFalse();
            assertThat(carsPage.checkIntegrationCarRowData(plate, brand, model, price)).isFalse();
        }

        @Test
        @DisplayName("Should edit an existing car")
        public void shouldEditCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            createCarForTest(plate, "OldBrand", "OldModel", "50.00");

            CarFormPage form = carsPage.clickEditCarButton(plate);

            assertThat(form.getFormTitle()).isEqualTo("Editar Carro");

            String newBrand = "Ford";
            String newModel = "Mustang X";
            String newPrice = "200.00";
            form.fillForm(plate, newBrand, newModel, newPrice);

            carsPage = form.clickSubmit();

            assertThat(carsPage.checkIntegrationCarRowData(plate, newBrand, newModel, newPrice)).isTrue();
        }

        @Test
        @DisplayName("Should cancel process to edit an existing car")
        public void shouldCancelEditCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            createCarForTest(plate, "OldBrand", "OldModel", "50.00");

            CarFormPage form = carsPage.clickEditCarButton(plate);

            assertThat(form.getFormTitle()).isEqualTo("Editar Carro");

            String newModel = "Subaru X";
            String newPrice = "200.00";
            form.fillForm(plate, "OldBrand", newModel, newPrice);

            carsPage = form.clickCancel();

            assertThat(carsPage.checkIntegrationCarRowData(plate, "OldBrand", newModel, newPrice)).isFalse();
        }

        @Test
        @DisplayName("Should delete existing car")
        public void shouldDeleteCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            createCarForTest(plate, "OldBrand", "OldModel", "50.00");

            DeleteCarModal modal = carsPage.clickDeleteCarButton(plate);

            assertThat(modal.getConfirmationMessage()).contains("Tem certeza que deseja excluir").contains(plate);

            carsPage = modal.clickConfirmButton();
            assertThat(carsPage.isCarListed(plate)).isFalse();
        }

        @Test
        @DisplayName("Should cancel process to delete existing car")
        public void shouldCancelDeleteCar() {
            String plate = faker.regexify("[A-Z]{3}[0-9][A-Z][0-9]{2}");
            createCarForTest(plate, "OldBrand", "OldModel", "50.00");

            DeleteCarModal modal = carsPage.clickDeleteCarButton(plate);

            assertThat(modal.getConfirmationMessage()).contains("Tem certeza que deseja excluir").contains(plate);

            carsPage = modal.clickCancelButton();
            assertThat(carsPage.isCarListed(plate)).isTrue();
        }
    }

    private void createCarForTest(String plate, String brand, String model, String price) {
        CarFormPage form = carsPage.clickAddCarButton();
        form.fillForm(plate, brand, model, price);
        carsPage = form.clickSubmit();
    }
}

