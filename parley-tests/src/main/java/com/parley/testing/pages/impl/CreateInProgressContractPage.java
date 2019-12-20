package com.parley.testing.pages.impl;

import com.parley.testing.model.contracts.ContractField;
import com.parley.testing.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class CreateInProgressContractPage extends AbstractPage {

    private static final By CONTRACT_TITLE = By.id("contractTitle");
    private static final By CONTRACT_DUE_DATE = By.id("dueDate");
    private static final By CONTRACT_VALUE_INPUT = By.id("contractValue");
    private static final By COUNTERPARTY_PAPER_CHECKBOX = By.id("counterpartyPaper");
    private static final By COUNTERPARTY_ORGANIZATION = By.id("counterpartyOrganization");
    private static final By COUNTERPARTY_CN = By.id("counterpartyChiefNegotiator");
    private static final By CONTRACTING_REGION = By.id("contractingRegion");
    private static final By CONTRACTING_COUNTRY = By.id("contractingCountry");
    private static final By CONTRACT_ENTITY = By.id("contractEntity");
    private static final By CONTRACTING_DEPARTMENT = By.id("ContractingDepartment");
    private static final By CONTRACT_CATEGORY = By.id("contractCategory");
    private static final By CONTRACT_TYPE = By.id("contractType");
    private static final By MULTISELECT_ITEM = By.xpath("//div[contains(@class, 'multi-select') and contains(@class ,'option')]/label/div");

    private static final By SAVE_BUTTON = By.xpath("//button[contains(text(), 'SAVE')]");

    public CreateInProgressContractPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    @Override
    public void checkCurrentPage() throws Throwable {
        getDriver().switchTo().activeElement();
        waitUntilElementIsEnabled(COUNTERPARTY_ORGANIZATION);
    }

    public void createContract(List<ContractField> fields) throws InterruptedException {
        for (ContractField contractField : fields) {
            WebElement webElement = findElement(contractField.getSelector());
            if (ContractField.ContractFieldType.SELECT.equals(contractField.getType())) {
                Select select = new Select(webElement);
                select.selectByVisibleText(contractField.getValue());
            } else if (ContractField.ContractFieldType.CHECKBOX.equals(contractField.getType()) || ContractField.ContractFieldType.RADIO_BUTTON.equals(contractField.getType())) {
                webElement.click();
            } else if (ContractField.ContractFieldType.MULTI_SELECT.equals(contractField.getType())) {
                String [] values = contractField.getValue().split(",");
                for(String value : values){
                    Thread.sleep(3000);
                    webElement.click();
                    webElement.sendKeys(contractField.getValue());
                    webElement.findElement(MULTISELECT_ITEM).click();
                }
            } else {
                webElement.sendKeys(contractField.getValue());
            }
        }
        click(SAVE_BUTTON);
        WebDriverWait wait = new WebDriverWait(getDriver(), 5);
        wait.until(ExpectedConditions.elementToBeClickable(InProgressContractPage.CONTRACT_MENU));
    }

    public void createAcmeContract() throws InterruptedException {
        List<ContractField> fields = new ArrayList<ContractField>();
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACT_TITLE, "TestContract"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, COUNTERPARTY_ORGANIZATION, "Roman art"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, COUNTERPARTY_CN, "victoria+ccn@parleypro.com"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACTING_REGION, "NNAM - North America"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACTING_COUNTRY, "CA - Canada"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACT_ENTITY, "AACME Inc Air, Limited"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACTING_DEPARTMENT, "GGlobal Operations"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACT_CATEGORY, "CConsultancy"));
        fields.add(new ContractField(ContractField.ContractFieldType.MULTI_SELECT, CONTRACT_TYPE, "Consolidation Services"));
        createContract(fields);
    }

    public void createSimpleContract() throws InterruptedException {
        List<ContractField> fields = new ArrayList<ContractField>();
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, CONTRACT_TITLE, "TestContract"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, COUNTERPARTY_ORGANIZATION, "RomanArt"));
        fields.add(new ContractField(ContractField.ContractFieldType.INPUT, COUNTERPARTY_CN, "victoria+ccn@parleypro.com"));
        createContract(fields);
    }

}
