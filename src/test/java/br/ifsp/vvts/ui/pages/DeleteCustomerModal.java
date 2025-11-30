package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DeleteCustomerModal extends BasePage{

    private final By modalContainer = By.tagName("app-confirmation-dialog");
    private final By title = By.cssSelector("h2[mat-dialog-title]");
    private final By contentMessage = By.cssSelector("mat-dialog-content p");
    private final By confirmButton = By.cssSelector("button[color='warn']");
    private final By cancelButton = By.xpath("//button[.//span[contains(text(), 'Cancelar')]]");

    public DeleteCustomerModal(WebDriver driver) {
        super(driver);
        waitForElement(modalContainer);
    }

    public String getTitle() {
        return getText(title);
    }

    public String getConfirmationMessage() {
        return getText(contentMessage);
    }

    public CustomerPage clickConfirmButton() {
        click(confirmButton);
        return new CustomerPage(driver);
    }

    public CustomerPage clickCancelButton() {
        click(cancelButton);
        return new CustomerPage(driver);
    }
}
