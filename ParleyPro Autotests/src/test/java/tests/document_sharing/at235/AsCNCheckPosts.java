package tests.document_sharing.at235;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedDiscussionPDF;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AsCNCheckPosts
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AsCNCheckPosts.class);


    @Test
    @Description("As my team CN check that only external posts are visible")
    public void asCNCheckPosts()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-235_PDF_share");

        new OpenedDiscussionPDF();

        logger.info("Check that only external posts are visible...");
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(3)); // total amount of posts
        $$(".documents-pdf-discussion-post").first().find(".internal").shouldBe(Condition.visible); // first post is internal
        $$(".documents-pdf-discussion-post").get(1).find(".external").shouldBe(Condition.visible); // second post is external
        $$(".documents-pdf-discussion-post").last().find(".external").shouldBe(Condition.visible); // third post is external

        $(withText("internal comment by CCN")).shouldNotBe(Condition.visible); // internal post by ccn should not be visible
        $(withText("external post by CCN")).shouldBe(Condition.visible);
        $(withText("PDF // discussion by CN")).shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }
}
