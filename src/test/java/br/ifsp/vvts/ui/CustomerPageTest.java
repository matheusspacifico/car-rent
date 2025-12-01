package br.ifsp.vvts.ui;

import br.ifsp.vvts.infra.persistence.repository.CustomerRepository;
import br.ifsp.vvts.ui.pages.CustomerFormPage;
import br.ifsp.vvts.ui.pages.CustomerPage;
import br.ifsp.vvts.ui.pages.DeleteCustomerModal;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
public class CustomerPageTest extends AuthenticatedBaseUiTest {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerPage customerPage;

    @BeforeEach
    public void navigateToCustomerPage() {
        navigateTo("/customers");
        customerPage = new CustomerPage(driver);
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
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
            String cpf = "995.527.810-27";

            CustomerFormPage form = customerPage.clickAddCustomerButton();
            form.fillForm(name, cpf);
            customerPage = form.clickCancel();

            assertThat(customerPage.isCustomerListed(cpf)).isFalse();
            assertThat(customerPage.checkCustomerRowData(name, cpf)).isFalse();
        }

        @Test
        @DisplayName("Should edit an existing customer")
        public void shouldEditCustomer() {
            String cpf = "044.240.980-01";
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
            String cpf = "896.159.910-07";
            String originalName = "Original Name";
            createCustomerForTest(originalName, cpf);

            CustomerFormPage form = customerPage.clickEditCustomerButton(cpf);

            form.setName("Should Not Save This Name");
            customerPage = form.clickCancel();

            assertThat(customerPage.checkCustomerRowData(originalName, cpf)).isTrue();
        }

        @Test
        @DisplayName("Should delete existing customer")
        public void shouldDeleteCustomer() {
            String cpf = "774.217.830-32";
            createCustomerForTest("mim remova", cpf);

            DeleteCustomerModal modal = customerPage.clickDeleteCustomerButton(cpf);

            assertThat(modal.getConfirmationMessage()).contains("Tem certeza que deseja excluir").contains(cpf);

            customerPage = modal.clickConfirmButton();

            assertThat(customerPage.isCustomerListed(cpf)).isFalse();
        }

        @Test
        @DisplayName("Should cancel process to delete existing customer")
        public void shouldCancelDeleteCustomer() {
            String cpf = "397.889.700-87";
            createCustomerForTest("não mim remova", cpf);

            DeleteCustomerModal modal = customerPage.clickDeleteCustomerButton(cpf);

            customerPage = modal.clickCancelButton();

            assertThat(customerPage.isCustomerListed(cpf)).isTrue();
        }

    }

    private void createCustomerForTest(String name, String cpf) {
        CustomerFormPage form = customerPage.clickAddCustomerButton();
        form.fillForm(name, cpf);
        customerPage = form.clickSubmit();
    }

}