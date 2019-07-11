package com.parley.testing.pages.impl.dashboard;

import com.google.common.base.Strings;
import com.parley.testing.model.contracts.InProgressContract;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static java.lang.String.format;
import static org.testng.Assert.fail;

public class InProgressContractsPage extends AbstractDashboardPage {

    public static final By IN_PROGRESS_MENU = By.xpath("//a[contains(@class, 'page-menu__item_contracts') and contains(@class ,'state_active')]");
    public static final By CREATE_CONTRACT_BUTTON =  By.xpath("//button[contains(text(), 'NEW CONTRACT')]");

    //contract form
    public static final By CONTRACT_LIST_ITEM = By.xpath("//a[contains(@class, 'ui-tr') and contains(@class ,'contract-item')]");
    public static final By CONTRACT_TITLE = By.xpath(".//div[boolean(@title)]");
    public static final By CONTRACT_COUNTERPARTY = By.xpath(".//div[contains(@class, 'contracts-list__counterparty-cell')]");
    public static final By CONTRACT_CN = By.xpath(".//div[contains(@class, 'contracts-list__chief-negotiator-cell')]");
    public static final By CONTRACT_STAGE = By.xpath(".//div[contains(@class, 'contracts-list__cell-stage')]/div/div");
    public static final By CONTRACT_DISCUSSION_COUNT = By.xpath(".//span[contains(@class, 'discussion-indicator__count')]");

    //sort options

    public static final By SORT_BY_LAST_ACTIVITY = By.xpath("//div[contains(text(), 'Last activity')]");

    public InProgressContractsPage(WebDriver webDriverProvider) {
        super(webDriverProvider, IN_PROGRESS_MENU);
    }

    public void checkCreateContractButtonExists(){
        waitUntilElementIsDisplayed(CREATE_CONTRACT_BUTTON);
    }

    public void checkCreateContractButtonNotDisplayed(){
        checkElementDoesNotExist(CREATE_CONTRACT_BUTTON);
    }

    public List<InProgressContract> getInProgressContracts() throws InterruptedException {
        List<InProgressContract> list = new ArrayList<InProgressContract>();
        Thread.sleep(3000);
        List<WebElement> webElements = findElements(CONTRACT_LIST_ITEM);
        for(WebElement element : webElements){
            InProgressContract inProgressContract = new InProgressContract();
            inProgressContract.setLink(element.getAttribute("href"));
            inProgressContract.setTitle(element.findElement(CONTRACT_TITLE).getAttribute("title"));
            inProgressContract.setCounterparty(element.findElement(CONTRACT_COUNTERPARTY).getText());
            inProgressContract.setChiefNegotiator(element.findElement(CONTRACT_CN).getText());
            inProgressContract.setStage(element.findElement(CONTRACT_STAGE).getText());

            String discussions = element.findElement(CONTRACT_DISCUSSION_COUNT).getText();
            inProgressContract.setDiscussionCount((discussions != null) ? Integer.valueOf(discussions) : null);

            list.add(inProgressContract);
        }
        return list;
    }

    public void checkContractRequiredFieldsNotEmpty(List<InProgressContract> inProgressContracts){
        Assert.assertTrue((inProgressContracts != null) && (!inProgressContracts.isEmpty()));
        for(InProgressContract contract : inProgressContracts){
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getTitle()));
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getChiefNegotiator()));
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getStage()));
        }
    }

    public void checkOnlyContractWhereUserIsCNAreDisplayed(List<InProgressContract> inProgressContracts, String chiefNegotiator){
        for(InProgressContract contract : inProgressContracts){
            Assert.assertEquals(chiefNegotiator, contract.getChiefNegotiator());
        }
    }

    public void clickCreateContractButton(){
        move(CREATE_CONTRACT_BUTTON);
    }

    public void sortByLastActivity() throws InterruptedException {
        waitUntilElementIsDisplayed(SORT_BY_LAST_ACTIVITY);
        findElement(SORT_BY_LAST_ACTIVITY).click();
    }

}
