package forms;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import pages.Discussions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Form that appears after clicking APPROVAL lifecycle stage in case if document has open discussions
 */
public class ContractHasOpenDiscussions
{
    private static Logger logger = Logger.getLogger(ContractHasOpenDiscussions.class);


    public ContractHasOpenDiscussions()
    {
        $(".modal-body-title").shouldHave(Condition.text(" has open discussions"));
        $(".modal-body-description").shouldHave(Condition.text("To proceed with the approval process, all discussions must be closed"));
    }

    public Discussions clickViewDiscussions(String contractName)
    {
        $$(".modal-footer button").filterBy(Condition.exactText("View discussions")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("View discussions button was clicked");

        return new Discussions(contractName);
    }
}
