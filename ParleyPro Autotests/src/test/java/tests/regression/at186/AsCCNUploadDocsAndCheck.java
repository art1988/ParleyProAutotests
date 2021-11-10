package tests.regression.at186;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Cache;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.EXECUTED_CONTRACTS;
import static constants.SideBarItems.IN_PROGRESS_CONTRACTS;

@Listeners({ScreenShotOnFailListener.class})
public class AsCCNUploadDocsAndCheck
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private DashboardPage dashboardPage;
    private String contractName;

    private static Logger logger = Logger.getLogger(AsCCNUploadDocsAndCheck.class);


    @Test(priority = 1)
    public void getCounterpartyEmailAndLogin() throws InterruptedException
    {
        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        Thread.sleep(30_000);

        contractName = Cache.getInstance().getCachedContractTitle();
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "[qa-autotests] autotest_cn fn ln shared contract \"" + contractName + "\" with you"),
                "Email with subject: [qa-autotests] autotest_cn fn ln shared contract \"" + contractName + "\" with you" + " was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("Please click \"Get started\" below to review the contract.");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLOfRegistrationPage = bodyText.substring(start + 1, end);
        URLOfRegistrationPage = URLOfRegistrationPage.replaceAll("amp;", ""); // replace amp; symbol

        Selenide.open(URLOfRegistrationPage);

        LoginPage loginPage = new LoginPage();
        loginPage.setEmail(Const.PREDEFINED_CCN.getEmail());
        loginPage.setPassword(Const.PREDEFINED_CCN.getPassword());
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS});
    }

    @Test(priority = 2)
    public void uploadSecondDocAndCheckMenuOptions() throws InterruptedException
    {
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract(contractName);
        new OpenedContract().clickNewDocument().clickUploadMyTeamDocuments(Const.REGRESSION_DOC_AT141);
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Document dummyAT141 has been successfully uploaded."));

        logger.info("Checking menu options for the first document [AT-14]...");
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"AT-14\")').parent().parent().parent().find(\"div[class='document__menu']\").find(\"button\").click()");
        Thread.sleep(1_000);
        $$(".document__menu li[role='presentation']").filter(Condition.visible).shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("Download"));
        Screenshoter.makeScreenshot();

        Selenide.executeJavaScript("$('.document__menu li[role=\"presentation\"]:visible').hide()"); // close popup

        logger.info("Checking menu options for the second document [dummyAT141]...");
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"dummyAT141\")').parent().parent().parent().find(\"div[class='document__menu']\").find(\"button\").click()");
        Thread.sleep(1_000);
        $$(".document__menu li[role='presentation']").filter(Condition.visible).shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("Download"));
        Screenshoter.makeScreenshot();

        Selenide.executeJavaScript("$('.document__menu li[role=\"presentation\"]:visible').hide()"); // close popup
    }
}
