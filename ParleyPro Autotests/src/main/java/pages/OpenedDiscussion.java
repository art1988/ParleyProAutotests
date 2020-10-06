package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class OpenedDiscussion
{
    private SelenideElement countOfPosts = $(".documents-discussion-panel .discussion-indicator");
    private SelenideElement closeButton  = $(".documents-discussion-panel__close");



    private static Logger logger = Logger.getLogger(OpenedDiscussion.class);

    public OpenedDiscussion(String title)
    {
        $(".discussion-header__title-name").shouldBe(Condition.visible).shouldHave(Condition.exactText(title));
    }

    /**
     * Closes right panel of open discussion
     */
    public void close()
    {
        closeButton.click();

        logger.info("Close icon was clicked...");
    }

    public String getCountOfPosts()
    {
        return Selenide.executeJavaScript("return $('.documents-discussion-panel .discussion-indicator span').text()");
    }
}
