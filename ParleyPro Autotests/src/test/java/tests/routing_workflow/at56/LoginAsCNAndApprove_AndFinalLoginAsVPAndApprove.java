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
public class LoginAsCNAndApprove_AndFinalLoginAsVPAndApprove
{
    @Test(priority = 1)
    public void loginAsCNAndApprove() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("CTR-AT56");

        OpenedContract openedContract = new OpenedContract();
        // move contract to Negotiate
        openedContract.switchDocumentToNegotiate("AT-14", "", false)
                      .clickNext(false)
                      .setCounterpartyOrganization("CounterpartyAT")
                      .setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com")
                      .clickStart();

        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.text("is now visible to the Counterparty"));

        // move to the pre-signature approval
        openedContract.switchDocumentToPreSignApproval("AT-14").clickNext().clickStartApproval();
        $("#APPROVE_DOCUMENT").waitUntil(Condition.visible, 20_000);

        openedContract.clickApproveButton("AT-14").clickApproveButton();
        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.exactText("Documents have been approved"));

        Screenshoter.makeScreenshot();

        new DashboardPage().getSideBar().logout();
    }

    @Test(priority = 2)
    @Description("Final Approve by VP User. 500 happened here.")
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

        // logout as VP
        new DashboardPage().getSideBar().logout();
    }
}
