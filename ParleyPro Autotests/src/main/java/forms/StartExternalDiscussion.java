package forms;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents popup that appears after clicking POST of External discussion.
 * May also appear by clicking 'MAKE EXTERNAL' button of post.
 */
public class StartExternalDiscussion
{
    private static Logger logger = Logger.getLogger(StartExternalDiscussion.class);

    /**
     * Use this constructor if Counterparty is known
     * @param text partial text of the discussion
     * @param cpOrganization
     */
    public StartExternalDiscussion(String text, String cpOrganization)
    {
        $(".modal-body-title").shouldBe(visible)
                .shouldHave(or("message on popup", Condition.text("You are about to start an external discussion \"" + text + "\"."),
                                                         Condition.text("You are about to make an external post in")));

        $(".modal-body-description").shouldBe(visible)
                .shouldHave(or("message on popup", Condition.text("Once started, it will be visible to " + cpOrganization),
                                                         Condition.text("Once posted, it will be visible to " + cpOrganization)));
    }

    /**
     * Use this constructor if no Counterparty selected
     * @param text partial text of the discussion
     */
    public StartExternalDiscussion(String text)
    {
        $(".modal-body-title").shouldBe(visible)
                .shouldHave(or("message on popup", Condition.text("You are about to start an external discussion \"" + text + "\""),
                                                         Condition.text("You are about to make an external post in")));

        $(".modal-content .share-documents__message").shouldHave(exactText("Once started, this discussion and selected documents will be visible to the Counterparty"));
    }

    public void clickPostExternally()
    {
        $$(".modal-footer button").filterBy(text("POST EXTERNALLY")).first().shouldBe(visible, enabled).click();

        logger.info("POST EXTERNALLY button was clicked...");

        $(".modal-content").should(Condition.disappear);
    }

    public StartExternalDiscussion clickNext()
    {
        $$(".modal-footer button").filterBy(text("NEXT")).first().shouldBe(visible, enabled).click();

        logger.info("NEXT button was clicked...");

        return this;
    }

    public StartExternalDiscussion clickSend(String text, String cpOrganization)
    {
        // wait until spinner for CCN field will disappear
        $(".select__loading").should(disappear);

        $$(".modal-footer button").filterBy(text("SEND")).first().shouldBe(visible, enabled).click();

        return new StartExternalDiscussion(text, cpOrganization);
    }
}
