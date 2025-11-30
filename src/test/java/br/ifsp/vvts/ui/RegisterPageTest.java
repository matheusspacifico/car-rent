package br.ifsp.vvts.ui;

import br.ifsp.vvts.ui.pages.RegisterPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
@DisplayName("Register Page UI Tests")
class RegisterPageTest extends BaseUiTest {

    private RegisterPage registerPage;

    @BeforeEach
    void setUpPage() {
        registerPage = new RegisterPage(driver);
        registerPage.navigateTo(BASE_URL);
    }

    @Nested
    @DisplayName("Page Load Tests")
    class PageLoadTests {

        @Test
        @DisplayName("Should display register page correctly")
        void shouldDisplayRegisterPageCorrectly() {
            assertThat(registerPage.isOnRegisterPage()).isTrue();
            assertThat(registerPage.getCardTitle()).isEqualTo("Registrar");
        }

        @Test
        @DisplayName("Should have submit button disabled when form is empty")
        void shouldHaveSubmitButtonDisabledWhenFormIsEmpty() {
            assertThat(registerPage.isSubmitButtonEnabled()).isFalse();
        }
    }

}

