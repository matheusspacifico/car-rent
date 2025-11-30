package br.ifsp.vvts.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CustomerPage extends BasePage {

    private final By pageTitle = By.xpath("//h2[contains(text(), 'Clientes')]");
    private final By addCustomerButton = By.cssSelector("button[mattooltip='Adicionar Cliente']");
    private final By noCustomersMessage = By.className("no-customers-message");

    public CustomerPage(WebDriver driver) {
        super(driver);
        waitForElement(pageTitle);
    }

    public boolean isNoCustomersMessage() {
        return isElementVisible(noCustomersMessage);
    }

    public boolean isAddCustomerButton() {
        return isElementVisible(addCustomerButton);
    }

    public CustomerFormPage clickAddCustomerButton() {
        click(addCustomerButton);
        return new CustomerFormPage(driver);
    }

    private WebElement getRowByCpf(String cpf) {
        String specXpath = String.format(
                "//tr[.//td[contains(@class, 'mat-column-cpf') and normalize-space(text())='%s']]",
                cpf
        );
        return waitForElement(By.xpath(specXpath));
    }

    public boolean isCustomerListed(String cpf) {
        if (isNoCustomersMessage()) return false;
        try {
            return getRowByCpf(cpf).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public CustomerFormPage clickEditCustomerButton(String cpf) {
        WebElement row = getRowByCpf(cpf);
        WebElement editBtn = row.findElement(By.cssSelector("button[mattooltip='Editar']"));
        editBtn.click();
        return new CustomerFormPage(driver);
    }

    public DeleteCustomerModal clickDeleteCustomerButton(String cpf) {
        WebElement row = getRowByCpf(cpf);
        WebElement deleteBtn = row.findElement(By.cssSelector("button[mattooltip='Excluir']"));
        deleteBtn.click();
        return new DeleteCustomerModal(driver);
    }

    public boolean checkCustomerRowData(String expectedName, String expectedCpf) {
        try {
            WebElement row = getRowByCpf(expectedCpf);

            String actualName = row.findElement(By.cssSelector(".mat-column-name")).getText().trim();
            String actualCpf = row.findElement(By.cssSelector(".mat-column-cpf")).getText().trim();
            return actualName.equalsIgnoreCase(expectedName) && actualCpf.equals(expectedCpf);
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }
}