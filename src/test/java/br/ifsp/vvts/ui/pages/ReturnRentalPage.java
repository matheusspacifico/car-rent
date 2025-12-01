package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ReturnRentalPage extends BasePage {

    private final By pageTitle = By.cssSelector("mat-card-title");

    private final By returnDateInput = By.name("actualReturnDate");
    private final By damageCheckbox = By.name("needsMaintenance");
    private final By cleaningCheckbox = By.name("needsCleaning");

    private final By submitButton = By.cssSelector("button[type='submit']");

    public ReturnRentalPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(pageTitle, "Devolução"));
    }

    public void setReturnDate(String date) {
        type(returnDateInput, date);
    }

    public void setDamageRequired(boolean required) {
        toggleCheckbox(damageCheckbox, required);
    }

    public void setCleaningRequired(boolean required) {
        toggleCheckbox(cleaningCheckbox, required);
    }

    public RentalPage clickRegisterReturn() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        clickJS(submitButton);
        return new RentalPage(driver);
    }

    private void toggleCheckbox(By locator, boolean enable) {
        try {
            var checkbox = waitForElement(locator);
            String classAttribute = checkbox.getAttribute("class");
            boolean isChecked = classAttribute.contains("checkbox-checked");

            if (isChecked != enable) {
                clickJS(locator);
            }
        } catch (Exception e) {
            clickJS(locator);
        }
    }
}