package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DeleteCarModal extends BasePage{
    private final By modalContainer = By.tagName("app-confirmation-dialog");

    private final By title = By.cssSelector("h2[mat-dialog-title]");
    private final By contentMessage = By.cssSelector("mat-dialog-content p");
    private final By confirmButton = By.cssSelector("button[color='warn']");
    private final By cancelButton = By.xpath("//button[.//span[contains(text(), 'Cancelar')]]");

    public DeleteCarModal(WebDriver driver) {
        super(driver);
        waitForElement(modalContainer);
    }

    public String getTitle() {
        return getText(title);
    }

    public String getConfirmationMessage() {
        return getText(contentMessage);
    }

    public CarsPage clickConfirmButton() {
        click(confirmButton);
        return new CarsPage(driver);
    }

    public CarsPage clickCancelButton() {
        click(cancelButton);
        return new CarsPage(driver);
    }
}
