package tests.regression.at162;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AsCNMakeApprove
{
    private String contractName = "AT-162 Contract";
    private static Logger logger = Logger.getLogger(AsCNMakeApprove.class);

    @Test(priority = 1)
    public void loginBackAsCNMoveToApproveAndStartApproval()
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract(contractName);

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToPreSignApproval("AT-14")
                      .addParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail())
                      .clickStartApproval();

        logger.info("Assert that status was changed to APPROVAL for both contract and document...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Assert that button Approve is visible...");
        $("#APPROVE_DOCUMENT").shouldBe(Condition.visible).shouldBe(Condition.enabled);

        openedContract.clickApproveButton("AT-14").clickApproveButton();
        $(".notification-stack").shouldHave(Condition.exactText("Document AT-14 has been approved"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
