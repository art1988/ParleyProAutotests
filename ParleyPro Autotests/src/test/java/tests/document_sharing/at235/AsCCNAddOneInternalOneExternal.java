package tests.document_sharing.at235;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedContract;
import pages.OpenedDiscussionPDF;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AsCCNAddOneInternalOneExternal
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AsCCNAddOneInternalOneExternal.class);


    @Test
    @Description("As CCN add one internal post and one external")
    public void asCCNAddOneInternalOneExternal() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );

        sideBar = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS}).getSideBar();

        OpenedContract openedContract = new OpenedContract();

        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(1));
        $$(".documents-pdf-discussion-post__head-right .external").shouldHave(CollectionCondition.size(1));

        OpenedDiscussionPDF openedDiscussionPDF = new OpenedDiscussionPDF();

        logger.info("Adding internal new post as CCN...");
        openedDiscussionPDF.clickAddComment().setCommentForPDFDiscussion("internal comment by CCN").selectInternal().clickPost();
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(2)).last().find(".internal").shouldBe(Condition.visible);

        logger.info("Adding external new post as CCN...");
        openedDiscussionPDF.clickAddComment().setCommentForPDFDiscussion("external post by CCN").selectExternal().clickPost();
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(3)).last().find(".external").shouldBe(Condition.visible);

        $(withText("internal comment by CCN")).shouldBe(Condition.visible);
        $(withText("external post by CCN")).shouldBe(Condition.visible);

        sideBar.logout();
    }
}
