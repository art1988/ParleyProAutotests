package tests.requests.at219;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CreateParleyProAccountPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndSubmitRequest2
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndSubmitRequest.class);


    @Test(priority = 1)
    public void getEmailAndSign() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(30_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Role assignment: Requester"),
                "Email with subject: Role assignment: Requester was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("You have been designated as the Requester");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLFromSignInButton = bodyText.substring(start + 1, end);
        URLFromSignInButton = URLFromSignInButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Open URL from SIGN IN button...");
        Selenide.open(URLFromSignInButton);

        CreateParleyProAccountPage createParleyProAccountPage = new CreateParleyProAccountPage();

        createParleyProAccountPage.setFirstName("USER_AT219_Requester fn");
        createParleyProAccountPage.setLastName("USER_AT219_Requester ln");
        createParleyProAccountPage.setPassword("Parley650!");
        createParleyProAccountPage.setConfirmPassword("Parley650!");

        OpenedContract openedContract = createParleyProAccountPage.clickCreateAndSignIn(true);
        $$("button").filterBy(Condition.exactText("NEW REQUEST")).first().shouldBe(Condition.visible, Condition.enabled);
        sideBar = new SideBar(new SideBarItems[]{PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});
    }

    @Test(priority = 2)
    public void submitRequest() throws InterruptedException
    {
        $$("button").filterBy(Condition.exactText("NEW REQUEST")).first().click();

        ContractRequest contractRequest = new ContractRequest();

        contractRequest.setRequestTitle("request for at-219");
        contractRequest.setValueForSelect("ReqField_AT219_Trigger", "v1");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("request for at-219")).shouldBe(Condition.visible);

        logger.info("Logout as requester...");
        sideBar.logout();
    }
}
