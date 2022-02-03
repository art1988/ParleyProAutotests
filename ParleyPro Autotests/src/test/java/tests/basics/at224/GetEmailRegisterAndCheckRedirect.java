package tests.basics.at224;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import constants.SideBarItems;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CreateParleyProAccountPage;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.EmailChecker;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;


public class GetEmailRegisterAndCheckRedirect
{
    private static Logger logger = Logger.getLogger(GetEmailRegisterAndCheckRedirect.class);


    @Test(priority = 1)
    public void getEmailAndRegister() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract \"AT-224: share me\": document review request"),
                "Email with subject: 'Contract \"AT-224: share me\": document review request' was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("has requested you to review document");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLFromButton = bodyText.substring(start + 1, end);
        URLFromButton = URLFromButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Opening URL from REVIEW DOCUMENT button...");
        Selenide.open(URLFromButton);

        logger.info("Going to register new user...");
        CreateParleyProAccountPage createParleyProAccountPage = new CreateParleyProAccountPage();

        User user = Cache.getInstance().getCachedUser();

        createParleyProAccountPage.setFirstName(user.getFirstName());
        createParleyProAccountPage.setLastName(user.getLastName());
        createParleyProAccountPage.setPassword(user.getPassword());
        createParleyProAccountPage.setConfirmPassword(user.getPassword());

        logger.info("Click SIGN IN and check that user was redirected to contract...");
        OpenedContract openedContract = createParleyProAccountPage.clickCreateAndSignIn();
    }

    @Test(priority = 2)
    public void checkRedirectToContractOfRegisteredUser()
    {
        $(byText("AT-224: share me")).shouldBe(Condition.visible);
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "AA"));
        $$(".document__header .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "AA"));
        $(".document__body-content").shouldBe(Condition.visible);

        Assert.assertTrue(WebDriverRunner.getWebDriver().getCurrentUrl().contains(Cache.getInstance().getCachedContractId()),
                "Looks like that contract_id of opened contract is wrong !!! Redirect didn't work !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void logout()
    {
        new DashboardPage(new SideBarItems[]{PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD}).getSideBar().logout();
    }
}
