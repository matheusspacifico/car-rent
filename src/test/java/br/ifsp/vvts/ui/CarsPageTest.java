package br.ifsp.vvts.ui;

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
}