package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CarsPage extends BasePage {
    private final By pageTitle = By.xpath("//h2[contains(text(), 'Carros')]");
    private final By addCarButton = By.cssSelector("button[mattooltip='Adicionar Carro']");
    private final By noCarsMessage = By.className("no-cars-message");

    private final By tableRows = By.cssSelector("tr.mat-mdc-row");
    private final By licensePlateColumn = By.cssSelector(".mat-column-licensePlate");

    public CarsPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public boolean isAddCarButtonVisible() {
        return isElementVisible(addCarButton);
    }

    public boolean isNoCarsMessage() {
        return isElementVisible(noCarsMessage);
    }

    public boolean isCarListed(String licensePlate) {
        if (isNoCarsMessage()) return false;
        try {
            return getRowByLicensePlate(licensePlate).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement getRowByLicensePlate(String licensePlate) {
        String specXpath = String.format(
                "//tr[.//td[contains(@class, 'mat-column-licensePlate') and normalize-space(text())='%s']]",
                licensePlate
        );
        return waitForElement(By.xpath(specXpath));
    }

    public CarFormPage clickAddCarButton() {
        click(addCarButton);
        return new CarFormPage(driver);
    }

    public CarFormPage clickEditCarButton(String licensePlate) {
        WebElement row = getRowByLicensePlate(licensePlate);
        WebElement editButton = row.findElement(By.cssSelector("button[mattooltip='Editar']"));
        editButton.click();
        return new CarFormPage(driver);
    }

    public DeleteCarModal clickDeleteCarButton(String licensePlate) {
        WebElement row = getRowByLicensePlate(licensePlate);
        WebElement deleteButton = row.findElement(By.cssSelector("button[mattooltip='Excluir']"));
        deleteButton.click();
        return new DeleteCarModal(driver);
    }

    public boolean checkIntegrationCarRowData(String licensePlate, String expectedBrand, String expectedModel, String expectedPrice) {
        try {
            WebElement row = getRowByLicensePlate(licensePlate);

            String actualBrand = row.findElement(By.cssSelector(".mat-column-brand")).getText().trim();
            String actualModel = row.findElement(By.cssSelector(".mat-column-model")).getText().trim();
            String actualPrice = row.findElement(By.cssSelector(".mat-column-basePrice")).getText().trim();

            boolean brandMatch = actualBrand.equalsIgnoreCase(expectedBrand);
            boolean modelMatch = actualModel.equalsIgnoreCase(expectedModel);
            boolean priceMatch = actualPrice.contains(expectedPrice);

            return brandMatch && modelMatch && priceMatch;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }
}
