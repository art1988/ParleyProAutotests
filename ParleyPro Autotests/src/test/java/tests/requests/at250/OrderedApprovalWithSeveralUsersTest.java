package tests.requests.at250;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractRequest;
import forms.ResendInviteForm;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class OrderedApprovalWithSeveralUsersTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(OrderedApprovalWithSeveralUsersTest.class);


    @Test
    public void orderedApprovalWithSeveralUsersTest() throws InterruptedException
    {
        asRequesterAddRequest();
        asApprover2CheckRequest();
    }

    @Step
    public void asRequesterAddRequest() throws InterruptedException
    {
        sideBar = new DashboardPage().getSideBar();

        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("AT-250 Request");
        contractRequest.setValueForSelect("AT250-RequestF", "v1");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("AT-250 Request")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
        $(".contract-status").shouldBe(Condition.visible).shouldHave(Condition.exactText("APPROVAL"));

        /*
        logger.info("Wait for 60 sec... Check that email with subject 'Contract request approval' has arrived...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract request approval"),
                "Email with subject: 'Contract request approval' was not found !!!"); */
    }

    @Step
    public void asApprover2CheckRequest()
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_2.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.exactText("APPROVAL"));
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));

        logger.info("Checking users in header...");
        ElementsCollection userIcons = $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2));
        for( int i = 0; i < userIcons.size(); i++ )
        {
            StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
            jsCode.append("$('.contract-header-users__list .user').eq(" + i + ")[0].dispatchEvent(event);");
            Selenide.executeJavaScript(jsCode.toString());

            $(".rc-tooltip-inner .spinner").should(Condition.disappear);
            $(".contract-user__name").shouldBe(Condition.visible).shouldHave(Condition.text("Approval_User_"));

            Selenide.executeJavaScript("$('.rc-tooltip-inner').hide(); $('.rc-tooltip-inner').remove()");
        }

        logger.info("Check that only CANCEL button is visible in footer...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("CANCEL"));
    }
}
