package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ManageDiscussions
{
    private SelenideElement doneButton = $("._button.scheme_blue.size_lg");

    private Logger logger = Logger.getLogger(ManageDiscussions.class);


    public ManageDiscussions()
    {
        $(".modal-header").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Manage Discussions"));
        doneButton.shouldBe(Condition.enabled).shouldBe(Condition.visible);
    }

    public String getAmountOfExternalDiscussions()
    {
        String title = $(".scheme_external .manage-discussions-section__title").getText();

        return title.substring(0, title.indexOf("external")).trim();
    }

    public void clickDone()
    {
        doneButton.click();

        logger.info("DONE was clicked");
    }
}
