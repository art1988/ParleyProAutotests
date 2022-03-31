package tests.routing_workflow.at56;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsVPUserAndApprove
{
    @Test(priority = 1)
    @Description("This test logins as User with role Viewer Plus and approves.")
    public void loginAsVPUserAndApprove()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.VIEWER_PLUS_USER1.getEmail());
        loginPage.setPassword(Const.VIEWER_PLUS_USER1.getPassword());
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("CTR-AT56");

        new OpenedContract().clickApproveButton("AT-14").clickApproveButton();

        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.exactText("Documents have been approved"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logout()
    {
        new DashboardPage().getSideBar().logout();
    }
}
