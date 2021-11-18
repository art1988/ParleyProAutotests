package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * DISCUSSIONS board.
 * Represents opened DISCUSSIONS tab within one selected contract.
 * Usually appears after uploading of new version of document.
 * May be invoked by clicking DISCUSSIONS tab.
 */
public class Discussions
{
    private SelenideElement contractTitle = $(".contract-header__name");
    private SelenideElement documentsTab  = $(".menu-disc-doc__documents");


    private static Logger logger = Logger.getLogger(Discussions.class);

    public Discussions(String contractName)
    {
        logger.info("Waiting until spinner will disappear [up to 5 minutes]...");

        $(".spinner").waitUntil(Condition.disappear, 60_000 * 5); // (5 minutes) for very heavy docs with macros

        contractTitle.waitUntil(Condition.visible, 60_000).shouldHave(Condition.exactText(contractName));
    }

    /**
     * Expand discussion by title ( matches by text contains !!! )
     * @param titleOfDiscussion
     */
    public void expandDiscussion(String titleOfDiscussion)
    {
        $$(".discussion2-header__name").filterBy(Condition.text(titleOfDiscussion)).first().click();

        $(".discussion2:not(.discussion2_collapsed_yes)").shouldBe(Condition.visible);
        $(".discussion2:not(.discussion2_collapsed_yes)").find(".discussion2-original").shouldBe(Condition.visible);
        $(".discussion2:not(.discussion2_collapsed_yes)").find(".discussion2-post").shouldBe(Condition.visible);

        logger.info("Discussion '" + titleOfDiscussion + "' has been expanded...");
    }

    public String getDiscussionCount()
    {
        return $(".discussions-info .discussion-indicator__count").getText();
    }

    public OpenedContract clickDocumentsTab()
    {
        documentsTab.shouldBe(Condition.visible).click();

        logger.info("DOCUMENTS tab was clicked");

        return new OpenedContract();
    }
}
