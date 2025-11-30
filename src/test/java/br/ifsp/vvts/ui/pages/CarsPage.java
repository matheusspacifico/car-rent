package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CarsPage extends BasePage {
    private final By pageTitle = By.xpath("//h2[contains(text(),'Carros')]");
    private final By addCarButton = By.cssSelector("button[mattooltip='Adicionar Carro']");
    private final By noCarsMessage = By.className("no-cars-message");

    private final By tableRows = By.cssSelector("tr.mat-mdc-row")private final By licensePlateColumn = By.cssSelector(".mat-column-licensePlate");;

    public CarsPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public boolean isAddCarButton() {
        return isElementVisible(addCarButton);
    }

    public boolean isNoCarsMessage() {
        return isElementVisible(noCarsMessage);
    }

    public boolean isCarListed() {
        if (isNoCarsMessage()) return false;
        List<WebElement> plates = driver.findElements(licensePlateColumn);
        return !plates.isEmpty();
    }

    private WebElement getRowByLicensePlate(String licensePlate) {
        String specXpath = String.format(
                "//tr[.//td[contains(@class, 'mat-column-licensePlate') and normalize-space(text())='%s']]",
                licensePlate
        );
        return waitForElement(By.xpath(specXpath));
    }
}
