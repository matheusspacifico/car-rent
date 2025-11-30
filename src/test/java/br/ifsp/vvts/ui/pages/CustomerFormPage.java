package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CustomerFormPage extends BasePage {

    private final By pageTitle = By.tagName("mat-card-title");
    private final By nameInput = By.name("name");
    private final By cpfInput = By.name("cpf");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By cancelButton = By.xpath("//button[.//span[contains(text(), 'Cancelar')]]");

    public CustomerFormPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public String getFormTitle() {
        return getText(pageTitle);
    }

    public void setName(String name) {
        type(nameInput, name);
    }

    public void setCpf(String cpf) {
        WebElement input = waitForElement(cpfInput);
        if (input.isEnabled()) {
            input.clear();
            input.sendKeys(cpf);
        }
    }

    public void fillForm(String name, String cpf) {
        setName(name);
        setCpf(cpf);
    }

    public CustomerPage clickSubmit() {
        click(submitButton);
        return new CustomerPage(driver);
    }

    public CustomerPage clickCancel() {
        click(cancelButton);
        return new CustomerPage(driver);
    }
}