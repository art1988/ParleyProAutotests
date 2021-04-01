package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.subelements.ListOfPosts;

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

    /**
     * Expand discussion group by clicking arrow icon
     * @param groupType may be internal, queued or external
     * @return
     */
    public ListOfPosts expandDiscussionGroup(String groupType)
    {
        // Click by arrow icon to expand
        WebElement arrowIcon = Selenide.executeJavaScript("return $('.manage-discussions-section .manage-discussions-section__title:contains(\"" + groupType + "\")').parent().find(\".manage-discussions-section__arrow\")[0]");

        $(arrowIcon).click();

        return new ListOfPosts(groupType);
    }

    public void clickDone()
    {
        doneButton.click();

        logger.info("DONE was clicked");

        $(".manage-discussions-sections").should(Condition.disappear);
    }
}
