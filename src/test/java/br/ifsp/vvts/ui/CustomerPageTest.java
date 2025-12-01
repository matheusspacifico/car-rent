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

        @Test
        @DisplayName("Should edit an existing customer")
        public void shouldEditCustomer() {
            String cpf = "035.056.850-20";
            createCustomerForTest("Old Name", cpf);

            CustomerFormPage form = customerPage.clickEditCustomerButton(cpf);

            assertThat(form.getFormTitle()).isEqualTo("Editar Cliente");

            String newName = faker.name().fullName() + " Edited";
            form.fillForm(newName, cpf);

            customerPage = form.clickSubmit();

            assertThat(customerPage.checkCustomerRowData(newName, cpf)).isTrue();
        }

        @Test
        @DisplayName("Should cancel process to edit an existing customer")
        public void shouldCancelEditCustomer() {
            String cpf = "035.056.850-20";
            String originalName = "Original Name";
            createCustomerForTest(originalName, cpf);

            CustomerFormPage form = customerPage.clickEditCustomerButton(cpf);

            form.setName("Should Not Save This Name");
            customerPage = form.clickCancel();

            assertThat(customerPage.checkCustomerRowData(originalName, cpf)).isTrue();
        }
    }

    private void createCustomerForTest(String name, String cpf) {
        CustomerFormPage form = customerPage.clickAddCustomerButton();
        form.fillForm(name, cpf);
        customerPage = form.clickSubmit();
    }

}