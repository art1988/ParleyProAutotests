package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ContractInformation;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ExecutedContractsPage
{
    private SelenideElement searchBar                  = $(".contracts-search-input__text");
    private SelenideElement newContractButton          = $(".js-create-contract-btn");
    private SelenideElement downloadContractDataButton = $(".download-button");


    private static Logger logger = Logger.getLogger(ExecutedContractsPage.class);

    /**
     * If isBlank is true it means that no contracts were added.
     * User should see message "Welcome to your contracts! There are no executed contracts. You can start a new executed contract by clicking the button below"
     * and + NEW CONTRACT button.
     *
     * Otherwise user should see table of contracts that are executed
     * @param isBlank
     */
    public ExecutedContractsPage(boolean isBlank)
    {
        $(".spinner").should(Condition.disappear);

        /*if( isBlank )
        {
            $(".contracts__empty-greetings").waitUntil(Condition.visible, 7_000)
                    .shouldHave(Condition.exactText("Welcome to your contracts!"));
            $(".contracts__create").waitUntil(Condition.visible, 7_000)
                    .shouldHave(Condition.exactText("There are no executed contracts.\nYou can start a new executed  contract by clicking the button below\nNEW CONTRACT"));
        }
        else
        {
            $(".page-head__left").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Executed contracts"));
            $(".contracts-list__table").shouldBe(Condition.visible);
            $$(".contracts-list__table a").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        }*/

        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public ContractInformation clickNewContractButton()
    {
        newContractButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("+ NEW CONTRACT button was clicked");

        return new ContractInformation();
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

        downloadContractDataButton.shouldBe(Condition.visible, Condition.enabled).download();
    }

    /**
     * Click contract by contract name. Contract has some uploaded documents. Match by contains
     * @param contractName
     */
    public ContractInfo selectContract(String contractName)
    {
        Selenide.executeJavaScript("$('.contracts-list__table div:contains(\"" + contractName + "\")').click()");
        logger.info("Contract '" + contractName + "' was selected...");

        return new ContractInfo();
    }

    /**
     * Click contract by contract name. Returns Add Documents page because no documents were uploaded.
     * @param contractName
     * @return
     */
    public AddDocuments selectContractWithoutUploadedDoc(String contractName)
    {
        Selenide.executeJavaScript("$('.contracts-list__table div:contains(\"" + contractName + "\")').click()");
        logger.info("Contract '" + contractName + "' was selected...");

        return new AddDocuments();
    }
}
