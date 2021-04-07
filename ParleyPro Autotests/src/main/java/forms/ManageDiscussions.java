package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.subelements.ListOfPosts;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ManageDiscussions
{
    private SelenideElement makeExternalButton = $(".manage-discussions-footer-popup__foot ._button.scheme_blue.size_lg");
    private SelenideElement doneButton         = $("._button.scheme_blue.size_lg");

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

    public String getAmountOfInternalDiscussions()
    {
        String title = $(".scheme_internal .manage-discussions-section__title").getText();

        return title.substring(0, title.indexOf("internal")).trim();
    }

    public ManageDiscussions makeExternalAllInternalDiscussions()
    {
        $$(".scheme_internal button").filter(Condition.exactText("Make external")).get(0).click();

        return this;
    }

    public ManageDiscussions makeExternalAllQueuedDiscussions()
    {
        $$(".scheme_queued button").filter(Condition.exactText("Make external")).get(0).click();

        return this;
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

    /**
     * Click by MAKE EXTERNAL button for the first time.
     * The button appears in footer after clicking by 'make external' for list of posts.
     * Important: This method must be the first one in call chain of MAKE EXTERNAL !
     * This was told by business logic in https://parley.atlassian.net/browse/PAR-13772
     * @param contractName
     * @return SendInvitation form
     */
    public SendInvitation confirmMakeExternalForTheFirstTime(String contractName)
    {
        makeExternalButton.click();

        logger.info("MAKE EXTERNAL button was clicked for the first time");

        return new SendInvitation(contractName);
    }

    /**
     * Performs regular click by MAKE EXTERNAL button.
     * Important: The first call must be confirmMakeExternalForTheFirstTime !
     * @return
     */
    public ManageDiscussions confirmMakeExternalRegularly()
    {
        makeExternalButton.click();

        logger.info("MAKE EXTERNAL button was clicked");

        return this;
    }
}
