package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ContractInformation;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import pages.subelements.SideBar;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InProgressContractsPage
{
    private SideBar sideBar;
    private SelenideElement newContractButton          = $(".contracts__create button[type='button']"); // the same css class represents + NEW REQUEST button
    private SelenideElement searchBar                  = $(".contracts-search-input__text");
    private SelenideElement downloadContractDataButton = $(".contracts-head__download");


    private static Logger logger = Logger.getLogger(InProgressContractsPage.class);

    /**
     * If isBlank is true it means that no contracts were added.
     * User should see message "Welcome to your contracts! You can start a new contract by clicking the button below"
     * and + NEW CONTRACT button.
     *
     * Otherwise user should see table of contracts that are in progress
     * @param isBlank
     */
    public InProgressContractsPage(boolean isBlank)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        if( isBlank )
        {
            // Check for presence of 500 error
            // If button + NEW CONTRACT doesn't exist - it means that 500 occurred
            if( newContractButton.waitUntil(Condition.visible, 5_000).isDisplayed() == false )
            {
                logger.error("Looks like master is down ! 500 http code");
                throw new SkipException("Looks like master is down ! 500 http code"); // Skip all test suite
            }

            boolean hasImage = $(".contracts__empty-image").getCssValue("background").contains("images/2a54d69eccf09694948ac7afd0eea951.svg");

            // Check that blank page has greeting text "Welcome to your contracts!" ...
            $(".contracts__empty-greetings").shouldHave(Condition.exactText("Welcome to your contracts!"));

            // Check message on page. Depends of what role does user have: admin or requester
            if( newContractButton.getText().equals("NEW CONTRACT") )
            {
                $(".contracts__empty-message").shouldHave(Condition.exactText("You can start a new  contract by clicking the button below"));
            }
            else if( newContractButton.getText().equals("NEW REQUEST") )
            {
                $(".contracts__empty-message").shouldHave(Condition.exactText("You can start a new request by clicking the button below"));
            }
            else
            {
                logger.error("There is no [+ NEW CONTRACT] or [+ NEW REQUEST] button on page !!!");
                Assert.fail("There is no [+ NEW CONTRACT] or [+ NEW REQUEST] button on page !!!");
            }

            // and also has image
            Assert.assertTrue(hasImage);
        }
        else
        {
            $(".contracts-list__table").shouldBe(Condition.visible);
            $$(".contracts-list__table a").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        }

        sideBar = new SideBar();
    }

    public ContractInformation clickNewContractButton()
    {
        newContractButton.click();

        logger.info("+ NEW CONTRACT button was clicked");

        return new ContractInformation();
    }

    public ContractRequest clickNewRequestButton()
    {
        newContractButton.click();

        logger.info("+ NEW REQUEST button was clicked");

        return new ContractRequest();
    }

    /**
     * Click by contract title.
     * May return new page with 'Add Documents' or already uploaded contract ( OpenedContract )
     * @param contractName
     */
    public void selectContract(String contractName)
    {
        Selenide.executeJavaScript("$('.contracts-list__contract-name:contains(\"" + contractName + "\")').click()");

        logger.info("Contract '" + contractName + "' was selected");

        $(".spinner").waitUntil(Condition.disappear, 20_000);
        $(".document__body .spinner").waitUntil(Condition.disappear, 20_000);
    }

    public SideBar getSideBar()
    {
        return sideBar;
    }

    public void search(String searchString)
    {
        searchBar.setValue(searchString);
        searchBar.pressEnter();
    }

    /**
     * Click by 'Download contract data' icon. Usually it download *.csv file
     * @return
     * @throws FileNotFoundException
     */
    public void clickDownloadContractData() throws FileNotFoundException
    {
        logger.info("Download contract data button was clicked.");

        downloadContractDataButton.download();
    }
}
