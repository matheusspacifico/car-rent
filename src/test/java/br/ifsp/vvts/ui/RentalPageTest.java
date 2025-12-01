package br.ifsp.vvts.ui;

import br.ifsp.vvts.domain.model.rental.RentalStatus;
import br.ifsp.vvts.infra.persistence.entity.car.CarEntity;
import br.ifsp.vvts.infra.persistence.entity.car.LicensePlateEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CPFEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import br.ifsp.vvts.infra.persistence.entity.rental.RentalEntity;
import br.ifsp.vvts.infra.persistence.repository.CarRepository;
import br.ifsp.vvts.infra.persistence.repository.CustomerRepository;
import br.ifsp.vvts.infra.persistence.repository.RentalRepository;
import br.ifsp.vvts.ui.pages.RentalFormPage;
import br.ifsp.vvts.ui.pages.RentalPage;
import br.ifsp.vvts.ui.pages.ReturnRentalPage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class RentalPageTest extends AuthenticatedBaseUiTest {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private RentalPage rentalPage;

    private CarEntity savedCar;
    private CustomerEntity savedCustomer;

    @BeforeEach
    public void setupDataAndNavigate() {
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();
        createTestEntities();
        navigateTo("/rentals");
        rentalPage = new RentalPage(driver);
    }

    @SuppressWarnings("deprecation")
    private void createTestEntities() {
        CarEntity car = new CarEntity();
        car.setLicensePlate(new LicensePlateEmbeddable("ABC1234"));
        car.setBrand("Fiat");
        car.setModel("Uno");
        car.setBasePrice(100.00);
        savedCar = carRepository.save(car);

        CustomerEntity customer = new CustomerEntity();
        customer.setName("João Silva");
        customer.setCpf(new CPFEmbeddable("38625707890"));
        savedCustomer = customerRepository.save(customer);
    }

    @Nested
    @DisplayName("Page Load Tests")
    class PageLoadTests {
        @Test
        @DisplayName("Should display correct page title")
        public void shouldDisplayTitle() {
            assertThat(rentalPage.getPageTitle()).contains("Lista de Alugueis");
        }

        @Test
        @DisplayName("Should display Add button")
        public void shouldDisplayAddButton() {
            assertThat(rentalPage.isAddRentalButtonVisible()).isTrue();
        }
    }

    @Nested
    @DisplayName("Full Flow Tests (Happy Path)")
    class FullFlowTests {

        @Test
        @DisplayName("Should create a new rental successfully via Form")
        public void shouldCreateNewRental() {
            RentalFormPage form = rentalPage.clickAddRentalButton();

            form.fillCpf("38625707890");
            form.fillLicensePlate("ABC1234");
            form.setStartDate("01/12/2025");
            form.setEndDate("10/12/2025");
            form.toggleInsurance(true);

            rentalPage = form.clickSubmit();

            assertThat(rentalPage.isNoRentalsMessageVisible()).isFalse();
            assertThat(rentalPage.getRentalCount()).isEqualTo(1);
            assertThat(rentalPage.getFirstRowStatus()).isEqualTo("Ativo");
        }

        @Test
        @DisplayName("Should perform rental return flow successfully")
        public void shouldReturnCarSuccessfully() {
            createRentalInDb();
            driver.navigate().refresh();
            rentalPage = new RentalPage(driver);

            rentalPage.clickReturnFirstRental();
            ReturnRentalPage returnPage = new ReturnRentalPage(driver);

            returnPage.setReturnDate("12/12/2025");
            returnPage.setDamageRequired(false);
            returnPage.setCleaningRequired(true);

            rentalPage = returnPage.clickRegisterReturn();

            assertThat(rentalPage.getFirstRowStatus()).isEqualTo("Finalizado");
            assertThat(rentalPage.isReturnButtonEnabledInFirstRow()).isFalse();
        }
    }

    private void createRentalInDb() {
        rentalRepository.save(new RentalEntity(
                null,
                savedCustomer,
                savedCar,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BigDecimal.valueOf(500.00),
                RentalStatus.ACTIVE
        ));
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should disable submit button when form is empty")
        public void shouldKeepButtonDisabled() {
            RentalFormPage form = rentalPage.clickAddRentalButton();

            assertThat(form.isSubmitButtonEnabled())
                    .as("Botão deve estar desabilitado se o form está vazio")
                    .isFalse();
        }

        @Test
        @DisplayName("Should show error messages for required fields")
        public void shouldShowRequiredErrors() {
            RentalFormPage form = rentalPage.clickAddRentalButton();

            form.touchField("cpf");
            form.touchField("licensePlate");
            form.touchField("startDate");
            form.touchField("endDate");

            assertThat(form.hasErrorMessage("O CPF é obrigatório")).isTrue();
            assertThat(form.hasErrorMessage("Placa é obrigatória")).isTrue();
            assertThat(form.hasErrorMessage("A data de retirada é obrigatória")).isTrue();
        }

        @Test
        @DisplayName("Should cancel creation and return to list")
        public void shouldCancelCreation() {
            RentalFormPage form = rentalPage.clickAddRentalButton();

            form.fillLicensePlate("ABC1234");

            rentalPage = form.clickCancel();

            assertThat(rentalPage.getPageTitle()).contains("Lista de Alugueis");
            assertThat(rentalPage.getRentalCount()).isEqualTo(0);
        }
    }
}