package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String PAGE_URL = "/login";

    private final By emailInput = By.cssSelector("input[formControlName='username']");
    private final By passwordInput = By.cssSelector("input[formControlName='password']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By registerLink = By.cssSelector("a[routerLink='/register']");
    private final By cardTitle = By.cssSelector("mat-card-title");
    private final By emailRequiredError = By.xpath("//mat-error[contains(text(), 'Email é obrigatório')]");
    private final By emailInvalidError = By.xpath("//mat-error[contains(text(), 'Email inválido')]");
    private final By passwordRequiredError = By.xpath("//mat-error[contains(text(), 'Senha é obrigatória')]");
    private final By snackBarMessage = By.cssSelector("simple-snack-bar span.mat-mdc-snack-bar-label");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateTo(String baseUrl) {
        driver.get(baseUrl + PAGE_URL);
        waitForPageLoad();
        return this;
    }

    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public LoginPage clickSubmitButton() {
        click(submitButton);
        return this;
    }

    public RegisterPage clickRegisterLink() {
        click(registerLink);
        return new RegisterPage(driver);
    }

    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmitButton();
        return this;
    }

    public boolean isSubmitButtonEnabled() {
        return isElementEnabled(submitButton);
    }

    public boolean isEmailRequiredErrorVisible() {
        return isElementVisible(emailRequiredError);
    }

    public boolean isEmailInvalidErrorVisible() {
        return isElementVisible(emailInvalidError);
    }

    public boolean isPasswordRequiredErrorVisible() {
        return isElementVisible(passwordRequiredError);
    }

    public String getCardTitle() {
        return getText(cardTitle);
    }

    public boolean isSnackBarVisible() {
        return isElementVisible(snackBarMessage);
    }

    public String getSnackBarMessage() {
        return getText(snackBarMessage);
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains(PAGE_URL);
    }

    public boolean isRedirectedToRentals() {
        return waitForUrl("/rentals");
    }

    public void triggerEmailValidation() {
        click(emailInput);
        click(passwordInput);
    }

    public void triggerPasswordValidation() {
        click(passwordInput);
        click(emailInput);
    }
}

