package tests.routing_workflow.at56;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CompleteSignAsCN
{
    private static Logger logger = Logger.getLogger(CompleteSignAsCN.class);

    @Test
    public void completeSignAsCN() throws FileNotFoundException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("CTR-AT56");

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToSign("AT-14", false).clickStart();

        logger.info("Assert that status was changed to SIGN...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));

        ContractInfo signContractInfo = openedContract.clickCompleteSign("AT-14").clickComplete();

        signContractInfo.setSignatureDate();
        signContractInfo.setEffectiveDate();
        signContractInfo.clickSave();

        logger.info("Assert update notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract has been updated."));

        logger.info("Assert that status was changed to MANAGED...");
        $(".lifecycle").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("MANAGED"));

        Screenshoter.makeScreenshot();
    }
}
