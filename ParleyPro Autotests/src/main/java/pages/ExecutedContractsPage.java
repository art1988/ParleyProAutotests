package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ExecutedContractsPage
{
    private SelenideElement title     = $(".page-head__left");
    private SelenideElement searchBar = $(".contracts-search-input__text");


    private static Logger logger = Logger.getLogger(ExecutedContractsPage.class);

    public ExecutedContractsPage()
    {
        title.waitUntil(Condition.visible, 6_000).shouldHave(Condition.text("Executed contracts"));
    }

    public void search(String searchString)
    {
        searchBar.setValue(searchString);
        searchBar.pressEnter();
    }

    /**
     * Click contract by contract name
     * @param contractName
     */
    public ContractInfo selectContract(String contractName)
    {
        Selenide.executeJavaScript("$('.contracts-list__table div:contains(\"" + contractName + "\")').click()");
        logger.info("Contract '" + contractName + "' was selected...");

        return new ContractInfo();
    }
}
