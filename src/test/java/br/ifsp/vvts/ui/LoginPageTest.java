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

    @Nested
    @DisplayName("Valid Input Tests")
    class ValidInputTests {

        @Test
        @DisplayName("Should enable submit button with valid email and password")
        void shouldEnableSubmitButtonWithValidEmailAndPassword() {
            String validEmail = generateValidEmail();
            String validPassword = generateValidPassword();

            loginPage.enterEmail(validEmail);
            loginPage.enterPassword(validPassword);

            assertThat(loginPage.isSubmitButtonEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfullyWithValidCredentials() {
            String email = generateValidEmail();
            String password = generateValidPassword();

            createTestUser(email, password);

            loginPage.login(email, password);

            assertThat(loginPage.isRedirectedToRentals()).isTrue();
        }

        @Test
        @DisplayName("Should navigate to register page when clicking register link")
        void shouldNavigateToRegisterPageWhenClickingRegisterLink() {
            var registerPage = loginPage.clickRegisterLink();

            assertThat(registerPage.isOnRegisterPage()).isTrue();
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {

        @Test
        @DisplayName("Should show error for empty email")
        void shouldShowErrorForEmptyEmail() {
            loginPage.triggerEmailValidation();

            assertThat(loginPage.isEmailRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for invalid email format")
        void shouldShowErrorForInvalidEmailFormat() {
            String invalidEmail = generateInvalidEmail();

            loginPage.enterEmail(invalidEmail);
            loginPage.triggerPasswordValidation();

            assertThat(loginPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email without @ symbol")
        void shouldShowErrorForEmailWithoutAtSymbol() {
            loginPage.enterEmail("invalidemail.com");
            loginPage.triggerPasswordValidation();

            assertThat(loginPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email without domain")
        void shouldShowErrorForEmailWithoutDomain() {
            loginPage.enterEmail("invalid@");
            loginPage.triggerPasswordValidation();

            assertThat(loginPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for empty password")
        void shouldShowErrorForEmptyPassword() {
            loginPage.triggerPasswordValidation();

            assertThat(loginPage.isPasswordRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should disable submit button with only email filled")
        void shouldDisableSubmitButtonWithOnlyEmailFilled() {
            String validEmail = generateValidEmail();

            loginPage.enterEmail(validEmail);

            assertThat(loginPage.isSubmitButtonEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should disable submit button with only password filled")
        void shouldDisableSubmitButtonWithOnlyPasswordFilled() {
            String validPassword = generateValidPassword();

            loginPage.enterPassword(validPassword);

            assertThat(loginPage.isSubmitButtonEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should disable submit button with invalid email")
        void shouldDisableSubmitButtonWithInvalidEmail() {
            String invalidEmail = generateInvalidEmail();
            String validPassword = generateValidPassword();

            loginPage.enterEmail(invalidEmail);
            loginPage.enterPassword(validPassword);

            assertThat(loginPage.isSubmitButtonEnabled()).isFalse();
        }

    }

}

