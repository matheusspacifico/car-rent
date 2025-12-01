package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RentalPage extends BasePage {

    private final By pageTitle = By.xpath("//h2[contains(text(), 'Lista de Alugueis')]");
    private final By addRentalButton = By.cssSelector("button[mattooltip='Adicionar Aluguel']");
    private final By noRentalsMessage = By.className("no-rentals-message");

    private final By tableRows = By.cssSelector("table.full-width-table tr[mat-row]");

    private final By cellStatus = By.cssSelector(".mat-column-status");
    private final By buttonReturn = By.cssSelector("button[mattooltip='Retornar Veiculo']");

    public RentalPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isAddRentalButtonVisible() {
        return isElementVisible(addRentalButton);
    }

    public boolean isNoRentalsMessageVisible() {
        return isElementVisible(noRentalsMessage);
    }

    public int getRentalCount() {
        if (isNoRentalsMessageVisible()) return 0;
        try {
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public RentalFormPage clickAddRentalButton() {
        click(addRentalButton);
        return new RentalFormPage(driver);
    }


    public void clickReturnFirstRental() {
        WebElement firstRow = waitForElement(tableRows);
        WebElement btn = firstRow.findElement(buttonReturn);
        btn.click();
    }


    public String getFirstRowStatus() {
        if (isNoRentalsMessageVisible()) return "";
        WebElement firstRow = waitForElement(tableRows);
        return firstRow.findElement(cellStatus).getText().trim();
    }

    public boolean isReturnButtonEnabledInFirstRow() {
        if (isNoRentalsMessageVisible()) return false;
        WebElement firstRow = waitForElement(tableRows);
        WebElement btn = firstRow.findElement(buttonReturn);
        String classes = btn.getAttribute("class");
        return btn.isEnabled() && !classes.contains("disabled-button");
    }

}