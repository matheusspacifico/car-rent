package br.ifsp.vvts.ui;

import br.ifsp.vvts.ui.pages.CustomerFormPage;
import br.ifsp.vvts.ui.pages.CustomerPage;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class CustomerPageTest extends AuthenticatedBaseUiTest {

    private CustomerPage customerPage;

    @BeforeEach
    public void navigateToCustomerPage() {
        navigateTo("/customers");
        customerPage = new CustomerPage(driver);
    }

    @Nested
    @DisplayName("Page Load Tests")
    class PageLoadTests {
        @Test
        @DisplayName("Should display correct page title")
        public void shouldDisplayTitle() {
            assertThat(customerPage.getPageTitle()).isEqualTo("Clientes");
        }

        @Test
        @DisplayName("Should display Add button")
        public void shouldDisplayAddButton() {
            assertThat(customerPage.isAddCustomerButton()).isTrue();
        }
    }

    @Nested
    @DisplayName("Customer Operations Tests")
    class OperationsTests {

        @Test
        @DisplayName("Should create a new customer successfully")
        public void shouldCreateCustomer() {
            String name = faker.name().fullName();
            String cpf = "035.056.850-20";

            CustomerFormPage form = customerPage.clickAddCustomerButton();
            form.fillForm(name, cpf);
            customerPage = form.clickSubmit();

            assertThat(customerPage.isCustomerListed(cpf)).isTrue();

            assertThat(customerPage.checkCustomerRowData(name, cpf)).isTrue();
        }

        @Test
        @DisplayName("Should cancel process to create a new customer")
        public void shouldCancelCreateCustomer() {
            String name = faker.name().fullName();
            String cpf = "035.056.850-20";

            CustomerFormPage form = customerPage.clickAddCustomerButton();
            form.fillForm(name, cpf);
            customerPage = form.clickCancel();

            assertThat(customerPage.isCustomerListed(cpf)).isFalse();
            assertThat(customerPage.checkCustomerRowData(name, cpf)).isFalse();
        }
    }
}