package tests.regression.at162;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

/**
 * This test will be used in AT-162 _and_ in AT-165
 * Parameter contractNameEmail - just name of the contract that will be used in email subject
 */
@Listeners({ScreenShotOnFailListener.class})
public class GetEmailAddInternalDiscussionAsCCN
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";
    private String contractName;

    private static DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(GetEmailAddInternalDiscussionAsCCN.class);

    @Test(priority = 1)
    @Parameters("contractNameEmail")
    public void getEmailAndLoginAsCCN(String contractNameEmail) throws InterruptedException
    {
        contractName = contractNameEmail;

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "[qa-autotests] autotest_cn fn ln shared contract \"" + contractName + "\" with you"),
                "Email with subject: [qa-autotests] autotest_cn fn ln shared contract \"" + contractName + "\" with you" + " was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("Please click \"Get started\" below to review the contract.");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLFromButton = bodyText.substring(start + 1, end);
        URLFromButton = URLFromButton.replaceAll("amp;", ""); // remove amp; symbol

        Selenide.open(URLFromButton);

        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});
    }

    @Test(priority = 2)
    public void addInternalDiscussion() throws InterruptedException
    {
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract(contractName);

        new OpenedContract().clickByParagraph("Paragraph 2")
                            .selectInternal()
                            .setComment("CCN was here")
                            .clickPost();

        $(".notification-stack").shouldHave(Condition.text("Internal discussion"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void logoutAsCCN()
    {
        dashboardPage.getSideBar().logout();
    }
}
