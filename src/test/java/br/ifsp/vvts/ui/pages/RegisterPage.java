package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private static final String PAGE_URL = "/register";

    private final By nameInput = By.cssSelector("input[formControlName='name']");
    private final By lastnameInput = By.cssSelector("input[formControlName='lastname']");
    private final By emailInput = By.cssSelector("input[formControlName='email']");
    private final By passwordInput = By.cssSelector("input[formControlName='password']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By loginLink = By.cssSelector("a[routerLink='/login']");
    private final By cardTitle = By.cssSelector("mat-card-title");

    private final By nameRequiredError = By.xpath("//mat-error[contains(text(), 'Nome é obrigatório')]");
    private final By lastnameRequiredError = By.xpath("//mat-error[contains(text(), 'Sobrenome é obrigatório')]");
    private final By emailRequiredError = By.xpath("//mat-error[contains(text(), 'Email é obrigatório')]");
    private final By emailInvalidError = By.xpath("//mat-error[contains(text(), 'Email inválido')]");
    private final By passwordRequiredError = By.xpath("//mat-error[contains(text(), 'Senha é obrigatória')]");
    private final By snackBarMessage = By.cssSelector("simple-snack-bar span.mat-mdc-snack-bar-label");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage navigateTo(String baseUrl) {
        driver.get(baseUrl + PAGE_URL);
        waitForPageLoad();
        return this;
    }

    public RegisterPage enterName(String name) {
        type(nameInput, name);
        return this;
    }

    public RegisterPage enterLastname(String lastname) {
        type(lastnameInput, lastname);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public RegisterPage clickSubmitButton() {
        click(submitButton);
        return this;
    }

    public LoginPage clickLoginLink() {
        click(loginLink);
        return new LoginPage(driver);
    }

    public RegisterPage register(String name, String lastname, String email, String password) {
        enterName(name);
        enterLastname(lastname);
        enterEmail(email);
        enterPassword(password);
        clickSubmitButton();
        return this;
    }

    public boolean isSubmitButtonEnabled() {
        return isElementEnabled(submitButton);
    }

    public boolean isNameRequiredErrorVisible() {
        return isElementVisible(nameRequiredError);
    }

    public boolean isLastnameRequiredErrorVisible() {
        return isElementVisible(lastnameRequiredError);
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

    public boolean isOnRegisterPage() {
        return getCurrentUrl().contains(PAGE_URL);
    }

    public boolean isRedirectedToLogin() {
        return waitForUrl("/login");
    }

    public void triggerNameValidation() {
        click(nameInput);
        click(lastnameInput);
    }

    public void triggerLastnameValidation() {
        click(lastnameInput);
        click(emailInput);
    }

    public void triggerEmailValidation() {
        click(emailInput);
        click(passwordInput);
    }

    public void triggerPasswordValidation() {
        click(passwordInput);
        click(nameInput);
    }
}

