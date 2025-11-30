package br.ifsp.vvts.ui;

import br.ifsp.vvts.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UiTest")
@DisplayName("Login Page UI Tests")
class LoginPageTest extends BaseUiTest {

    private LoginPage loginPage;

    @BeforeEach
    void setUpPage() {
        loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
    }

    @Nested
    @DisplayName("Page Load Tests")
    class PageLoadTests {

        @Test
        @DisplayName("Should display login page correctly")
        void shouldDisplayLoginPageCorrectly() {
            assertThat(loginPage.isOnLoginPage()).isTrue();
            assertThat(loginPage.getCardTitle()).isEqualTo("Login");
        }

        @Test
        @DisplayName("Should have submit button disabled when form is empty")
        void shouldHaveSubmitButtonDisabledWhenFormIsEmpty() {
            assertThat(loginPage.isSubmitButtonEnabled()).isFalse();
        }
    }

}

