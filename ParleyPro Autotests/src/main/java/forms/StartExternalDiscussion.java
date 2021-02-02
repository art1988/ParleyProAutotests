package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents popup that appears after clicking POST of External discussion
 */
public class StartExternalDiscussion
{
    private SelenideElement postExternallyButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(StartExternalDiscussion.class);

    public StartExternalDiscussion(String text, String cpOrganization)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("You are about to start an external discussion \"" + text + "\"."));

        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Once started, it will be visible to " + cpOrganization));
    }

    public void clickPostExternally()
    {
        postExternallyButton.click();

        logger.info("POST EXTERNALLY button was clicked...");
    }
}
