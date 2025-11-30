package br.ifsp.vvts.ui;

import br.ifsp.vvts.security.user.User;
import br.ifsp.vvts.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;

public abstract class AuthenticatedBaseUiTest extends BaseUiTest {

    private static final String DEFAULT_TEST_PASSWORD = "Test@123456";
    
    private User authenticatedUser;
    private String authenticatedUserEmail;
    private String authenticatedUserPassword;

    @BeforeEach
    void authenticateUser() {
        authenticatedUserEmail = generateSimpleEmail();
        authenticatedUserPassword = DEFAULT_TEST_PASSWORD;

        authenticatedUser = createTestUser(authenticatedUserEmail, authenticatedUserPassword);

        performLogin();
    }

    private String generateSimpleEmail() {
        String username = faker.regexify("[a-z]{6,10}");
        String domain = faker.regexify("[a-z]{5,8}");
        return username + "@" + domain + ".com";
    }

    private void performLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(authenticatedUserEmail, authenticatedUserPassword);

        loginPage.isRedirectedToRentals();
    }

    protected User getAuthenticatedUser() {
        return authenticatedUser;
    }

    protected String getAuthenticatedUserEmail() {
        return authenticatedUserEmail;
    }

    protected String getAuthenticatedUserPassword() {
        return authenticatedUserPassword;
    }

    protected void navigateTo(String path) {
        driver.get(BASE_URL + path);
    }
}

