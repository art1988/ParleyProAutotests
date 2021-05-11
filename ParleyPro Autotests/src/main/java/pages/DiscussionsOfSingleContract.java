package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents opened DISCUSSIONS tab within one selected contract.
 * Usually appears after uploading of new version of document.
 */
public class DiscussionsOfSingleContract
{
    private SelenideElement contractTitle = $(".contract-header__name");
    private SelenideElement documentsTab  = $(".menu-disc-doc__documents");


    private static Logger logger = Logger.getLogger(DiscussionsOfSingleContract.class);

    public DiscussionsOfSingleContract(String contractName)
    {
        logger.info("Waiting until spinner will disappear [up to 5 minutes]...");
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 5); // (5 minutes) for very heavy docs with macros
        contractTitle.waitUntil(Condition.visible, 60_000).shouldHave(Condition.exactText(contractName));
    }

    public String getDiscussionCount()
    {
        return $(".discussions-info .discussion-indicator__count").getText();
    }

    public OpenedContract clickDocumentsTab()
    {
        documentsTab.waitUntil(Condition.visible, 7_000).click();

        logger.info("DOCUMENTS tab was clicked");

        return new OpenedContract();
    }
}
