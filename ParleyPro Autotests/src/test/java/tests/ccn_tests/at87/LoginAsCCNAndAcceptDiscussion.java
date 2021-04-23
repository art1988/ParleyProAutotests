package tests.ccn_tests.at87;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.AcceptTypes;
import constants.Const;
import constants.SideBarItems;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCNAndAcceptDiscussion
{
    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private DashboardPage dashboardPage;

    private Logger logger = Logger.getLogger(LoginAsCCNAndAcceptDiscussion.class);

    @Test(priority = 1)
    @Description("This test receives email for CCN, open that link and verifies that CCN doesn't see READY FOR SIGNATURE button.")
    public void getEmailAndLoginAsCCN() throws InterruptedException
    {
        logger.info("Waiting for 40 seconds to make sure that email has been delivered...");
        Thread.sleep(40_000);

        String emailSubject = "[qa-autotests] autotest_cn fn ln shared contract \"CCN: READY FOR SIGNATURE button contract\" with you";
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, emailSubject), "Email with subject: " + emailSubject + " was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("Please click \"Get started\" below to review the contract.");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLOfDocForCCN = bodyText.substring(start + 1, end);
        URLOfDocForCCN = URLOfDocForCCN.replaceAll("amp;", ""); // replace amp; symbol

        logger.info("Click that link...");
        Selenide.open(URLOfDocForCCN);

        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[] {SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        OpenedContract openedContract = new OpenedContract();
        logger.info("Assert that URL starts with https://counterpartyat.parleypro");
        Assert.assertTrue(WebDriverRunner.getWebDriver().getCurrentUrl().startsWith("https://counterpartyat.parleypro"));

        logger.info("Assert that opened contract has name 'CCN: READY FOR SIGNATURE button contract'...");
        Assert.assertEquals(openedContract.getContractName(), "CCN: READY FOR SIGNATURE button contract");

        logger.info("Assert that CCN doesn't see READY FOR SIGNATURE button...");
        $(".ready_to_sign").shouldNotBe(Condition.visible);

        logger.info("Assert that CCN see 1 discussion...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "1"); // on contract level
        Assert.assertEquals($(".document__header-info .discussion-indicator__count").waitUntil(Condition.visible, 7_000).getText(), "1"); // on doc level

        logger.info("Assert that user's avatars are correct for contract and document headers...");
        $$(".header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("CC", "AL"));
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("CC", "AL"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test accept one discussion as CCN and verifies that button READY FOR SIGNATURE becomes enabled.")
    public void acceptDiscussion()
    {
        OpenedContract openedContract = new OpenedContract();

        String paragraphToAccept = "Just new paragraph to initiate discussion";
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphToAccept);
        openedDiscussion.clickAccept(AcceptTypes.INSERT, paragraphToAccept).clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedDiscussion.close();

        logger.info("Assert that READY FOR SIGNATURE button is enabled...");
        $(".ready_to_sign").shouldBe(Condition.visible).shouldBe(Condition.enabled);

        logger.info("Assert that number of discussions for contract is empty");
        Assert.assertFalse($("user-icon-checked.contract-header__status .discussion-indicator__count").is(Condition.visible));

        logger.info("But discussion icon is still on page...");
        $$(".discussion-indicator").shouldHave(CollectionCondition.size(1));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test clicks Ready for signature button and checks that checkmark appears for user with CC avatar.")
    public void clickReadyForSignature()
    {
        new OpenedContract().clickReadyForSignature().clickOk();

        logger.info("Assert that checkmark was shown for CC...");
        $(".contract-header-users .contract-header-users__list .user-icon-checked").waitUntil(Condition.visible, 7_000);
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contract-header-users').find(\"span:contains('CC')\").find(\".user-icon-checked\").length"), Long.valueOf(1));
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contract-header-users').find(\"span:contains('AL')\").find(\".user-icon-checked\").length"), Long.valueOf(0));

        Screenshoter.makeScreenshot();

        dashboardPage.getSideBar().logout();
    }
}
