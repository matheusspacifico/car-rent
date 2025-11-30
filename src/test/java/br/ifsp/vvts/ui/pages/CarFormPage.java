package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CarFormPage extends BasePage {
    private final By pageTitle = By.tagName("mat-card-title");
    private final By licensePlateInput = By.name("licensePlate");
    private final By brandInput = By.name("brand");
    private final By modelInput = By.name("model");
    private final By basePriceInput = By.name("basePrice");

    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By cancelButton = By.xpath("//button[.//span[contains(text(), 'Cancelar')]]");

    public CarFormPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public void setLicensePlate(String plate) {
        if (!isElementEnabled(licensePlateInput)) return;
        type(licensePlateInput, plate);
    }

    public String getFormTitle() {
        return getText(pageTitle);
    }

    public void setBrand(String brand) {
        type(brandInput, brand);
    }

    public void setModel(String model) {
        type(modelInput, model);
    }

    public void setBasePrice(String price) {
        type(basePriceInput, price);
    }

    public void fillForm(String plate, String brand, String model, String price) {
        setLicensePlate(plate);
        setBrand(brand);
        setModel(model);
        setBasePrice(price);
    }

    public CarsPage clickSubmit() {
        click(submitButton);
        return new CarsPage(driver);
    }

    public CarsPage clickCancel() {
        click(cancelButton);
        return new CarsPage(driver);
    }
}
