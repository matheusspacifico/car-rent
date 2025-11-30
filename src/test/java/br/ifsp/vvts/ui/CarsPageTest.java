package br.ifsp.vvts.ui;

import br.ifsp.vvts.ui.pages.CarFormPage;
import br.ifsp.vvts.ui.pages.CarsPage;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class CarsPageTest extends AuthenticatedBaseUiTest {
    private CarsPage carsPage;

    @BeforeEach
    public void navigateToCarsPage() {
        navigateTo("/cars");
        carsPage = new CarsPage(driver);
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

    }
}