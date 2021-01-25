package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class RevertToOriginal
{
    private SelenideElement title = $(".modal-title");
    private SelenideElement closeDiscussionButton = $("._button.scheme_blue.size_lg");


    private static Logger logger = Logger.getLogger(RevertToOriginal.class);

    public RevertToOriginal()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to close this discussion?"));
        $(".modal-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("The contents of the document won't change because no text changes have been requested as a result of this discussion"));
        closeDiscussionButton.waitUntil(Condition.visible, 7_000);
    }

    public void clickCloseDiscussion()
    {
        closeDiscussionButton.click();

        logger.info("Close discussion button was clicked...");
    }
}
