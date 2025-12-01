package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RentalFormPage extends BasePage {

    private final By licensePlateInput = By.name("licensePlate");
    private final By cpfInput = By.name("cpf");
    private final By startDateInput = By.name("startDate");
    private final By endDateInput = By.name("endDate");

    private final By insuranceCheckbox = By.name("withInsurance");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By cancelButton = By.xpath("//button[contains(text(), 'Cancelar')]");

    public RentalFormPage(WebDriver driver) {
        super(driver);
        waitForUrl("/rentals/new");
    }

    public void fillLicensePlate(String plate) {
        type(licensePlateInput, plate);
    }

    public void fillCpf(String cpf) {
        click(cpfInput);
        type(cpfInput, cpf);
    }

    public void setStartDate(String date) {
        type(startDateInput, date);
    }

    public void setEndDate(String date) {
        type(endDateInput, date);
    }

    public void toggleInsurance(boolean enable) {
        try {
            var checkbox = waitForElement(insuranceCheckbox);
            String classAttribute = checkbox.getAttribute("class");
            boolean isChecked = classAttribute.contains("mat-mdc-checkbox-checked");

            if (isChecked != enable) {
                clickJS(insuranceCheckbox);
            }
        } catch (Exception e) {
            clickJS(insuranceCheckbox);
        }
    }

    public RentalPage clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        clickJS(submitButton);
        return new RentalPage(driver);
    }

    public RentalPage clickCancel() {
        clickJS(cancelButton);
        return new RentalPage(driver);
    }
}