package br.ifsp.vvts.ui;

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
}