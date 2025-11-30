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

    @Nested
    @DisplayName("Valid Input Tests")
    class ValidInputTests {

        @Test
        @DisplayName("Should enable submit button with all valid fields")
        void shouldEnableSubmitButtonWithAllValidFields() {
            String name = generateFirstName();
            String lastname = generateLastName();
            String email = generateValidEmail();
            String password = generateValidPassword();

            registerPage.enterName(name);
            registerPage.enterLastname(lastname);
            registerPage.enterEmail(email);
            registerPage.enterPassword(password);

            assertThat(registerPage.isSubmitButtonEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should register successfully with valid data")
        void shouldRegisterSuccessfullyWithValidData() {
            String name = generateFirstName();
            String lastname = generateLastName();
            String email = generateValidEmail();
            String password = generateValidPassword();

            registerPage.register(name, lastname, email, password);

            assertThat(registerPage.isSnackBarVisible()).isTrue();
            assertThat(registerPage.getSnackBarMessage()).contains("Registro efetuado com sucesso");
        }

        @Test
        @DisplayName("Should redirect to login page after successful registration")
        void shouldRedirectToLoginPageAfterSuccessfulRegistration() {
            String name = generateFirstName();
            String lastname = generateLastName();
            String email = generateValidEmail();
            String password = generateValidPassword();

            registerPage.register(name, lastname, email, password);

            assertThat(registerPage.isRedirectedToLogin()).isTrue();
        }

        @Test
        @DisplayName("Should navigate to login page when clicking login link")
        void shouldNavigateToLoginPageWhenClickingLoginLink() {
            var loginPage = registerPage.clickLoginLink();

            assertThat(loginPage.isOnLoginPage()).isTrue();
        }

        @Test
        @DisplayName("Should accept password with minimum length of 6 characters")
        void shouldAcceptPasswordWithMinimumLength() {
            String name = generateFirstName();
            String lastname = generateLastName();
            String email = generateValidEmail();
            String password = "123456";

            registerPage.enterName(name);
            registerPage.enterLastname(lastname);
            registerPage.enterEmail(email);
            registerPage.enterPassword(password);

            assertThat(registerPage.isSubmitButtonEnabled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {

        @Test
        @DisplayName("Should show error for empty name")
        void shouldShowErrorForEmptyName() {
            registerPage.triggerNameValidation();

            assertThat(registerPage.isNameRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for empty lastname")
        void shouldShowErrorForEmptyLastname() {
            registerPage.triggerLastnameValidation();

            assertThat(registerPage.isLastnameRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for empty email")
        void shouldShowErrorForEmptyEmail() {
            registerPage.triggerEmailValidation();

            assertThat(registerPage.isEmailRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for empty password")
        void shouldShowErrorForEmptyPassword() {
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isPasswordRequiredErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for invalid email format")
        void shouldShowErrorForInvalidEmailFormat() {
            String invalidEmail = generateInvalidEmail();

            registerPage.enterEmail(invalidEmail);
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email without @ symbol")
        void shouldShowErrorForEmailWithoutAtSymbol() {
            registerPage.enterEmail("invalidemail.com");
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email without domain")
        void shouldShowErrorForEmailWithoutDomain() {
            registerPage.enterEmail("invalid@");
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email without local part")
        void shouldShowErrorForEmailWithoutLocalPart() {
            registerPage.enterEmail("@domain.com");
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isEmailInvalidErrorVisible()).isTrue();
        }

        @Test
        @DisplayName("Should show error for email with multiple @ symbols")
        void shouldShowErrorForEmailWithMultipleAtSymbols() {
            registerPage.enterEmail("test@@domain.com");
            registerPage.triggerPasswordValidation();

            assertThat(registerPage.isEmailInvalidErrorVisible()).isTrue();
        }
    }

}

