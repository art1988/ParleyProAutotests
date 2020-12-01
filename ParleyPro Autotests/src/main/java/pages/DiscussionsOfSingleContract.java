package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents opened DISCUSSIONS tab within one selected contract
 */
public class DiscussionsOfSingleContract
{
    private SelenideElement contractTitle = $(".contract-header__name");
    private SelenideElement documentsTab  = $(".menu-disc-doc__documents");


    private static Logger logger = Logger.getLogger(DiscussionsOfSingleContract.class);

    public DiscussionsOfSingleContract(String contractName)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        contractTitle.waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(contractName));
    }

    public String getDiscussionCount()
    {
        return $(".discussions-info .discussion-indicator__count").getText();
    }

    public OpenedContract clickDocumentsTab()
    {
        documentsTab.click();

        logger.info("DOCUMENTS tab was clicked");

        return new OpenedContract();
    }
}
