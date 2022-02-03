package tests.regression.at140;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;



public class LoginAsCCNAgainAndCheck
{
    private static Logger logger = Logger.getLogger(LoginAsCCNAgainAndCheck.class);
    private DashboardPage dashboardPage;

    @Test(priority = 1)
    public void loginAsCCNAgainAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        logger.info("Checking that the internal discussions for the first and second paragraphs are still open...");
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(2));
        $$("ins").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder(" Added by CCN #1", " Added by CCN #2"));
    }

    @Test(priority = 2)
    public void createDiscussionForThirdParagraph() throws InterruptedException
    {
        new OpenedContract().clickByParagraph("Paragraph 3")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " Added by CCN #3"})
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Internal discussion"));

        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(3));
    }

    @Test(priority = 3)
    public void makeExternalThirdParagraph()
    {
        new OpenedContract().clickByDiscussionIcon("Paragraph 3")
                            .clickMakeExternal("Paragraph 3")
                            .clickPostExternally();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));

        // Logout
        dashboardPage.getSideBar().logout();
    }
}
