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
import pages.subelements.SearchFilterForm;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InProgressContractsPage
{
    private SelenideElement newContractButton          = $(".contracts__create button[type='button'], .js-create-contract-btn");
    private SelenideElement newRequestButton           = $("button:not(.js-create-contract-btn)");
    private SelenideElement searchBar                  = $(".contracts-search-input__text");
    private SelenideElement downloadContractDataButton = $(".download-button");


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
        $(".spinner").waitUntil(Condition.disappear, 45_000);

        /*if( isBlank )
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
            $(".contracts-list__table").waitUntil(Condition.visible, 20_000);
            $$(".contracts-list__table a").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        }*/
    }

    public ContractInformation clickNewContractButton()
    {
        newContractButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("+ NEW CONTRACT button was clicked");

        return new ContractInformation();
    }

    public ContractRequest clickNewRequestButton()
    {
        newRequestButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("+ NEW REQUEST button was clicked");

        return new ContractRequest();
    }

    /**
     * This method selects [contract] OR [request] by name.
     * May return new page with 'Add Documents' or already uploaded contract ( OpenedContract ).
     * Also may return 'Edit Contract Request' form in case of request.
     * @param contractName
     */
    public void selectContract(String contractName)
    {
        $(byText(contractName)).shouldBe(Condition.visible, Condition.enabled).click();

        //Selenide.executeJavaScript("$('.contracts-list__contract-name:contains(\"" + contractName + "\")').click()");

        logger.info("Contract '" + contractName + "' was selected");

        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
        $(".document__body .spinner").waitUntil(Condition.disappear, 60_000 * 2);
    }

    public Discussions clickDiscussionsTab()
    {
        $$(".tab-menu__item").filterBy(Condition.text("DISCUSSIONS")).first().click();

        logger.info("DISCUSSIONS tab was clicked...");

        return new Discussions();
    }

    public void search(String searchString)
    {
        searchBar.sendKeys(searchString);
        searchBar.pressEnter();
        $(".spinner").should(Condition.disappear);
    }

    public SearchFilterForm expandSearchFilter()
    {
        $(".contracts-search-input__toggle").click();

        logger.info("Search filter toggle has been clicked...");

        return new SearchFilterForm();
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

    /**
     * Click by 'Chief Negotiator' column to sort
     */
    public void clickByChiefNegotiatorColumn()
    {
        try { Thread.sleep(2_000); } catch (InterruptedException e) { logger.error("InterruptedException", e); }
        Selenide.executeJavaScript("$('.ui-td:contains(\"Chief Negotiator\")').click()");

        logger.info("'Chief Negotiator' column was clicked for sorting...");

        $(".spinner").waitUntil(Condition.appear, 4_000);
        $(".spinner").waitUntil(Condition.disappear, 10_000);
    }
}
