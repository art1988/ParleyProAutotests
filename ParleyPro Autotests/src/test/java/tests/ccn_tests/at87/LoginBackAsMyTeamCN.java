package tests.ccn_tests.at87;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.SignContract;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsMyTeamCN
{
    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private OpenedContract openedContract;
    private Logger logger = Logger.getLogger(LoginBackAsMyTeamCN.class);

    @Test(priority = 1)
    @Description("This test logins as CN of my team and verifies checkmark for CCN.")
    public void loginBackAsMyTeamCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("CCN: READY FOR SIGNATURE button contract");
        openedContract = new OpenedContract();

        logger.info("Assert that URL starts with https://qa-autotests.parleypro");
        Assert.assertTrue(WebDriverRunner.getWebDriver().getCurrentUrl().startsWith("https://qa-autotests.parleypro"));

        logger.info("Assert that CCN has checkmark...");
        $(".contract-header-users .contract-header-users__counterparty .user-icon-checked").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test completes sign. Goes to Executed contracts and checks CCN.")
    public void signAndCompleteSign()
    {
        String documentName = "AT-14";
        SignContract signContractForm = openedContract.switchDocumentToSign(documentName);

        try
        {
            signContractForm.clickStart();

            logger.info("Assert that file was downloaded...");
            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "AT-14.pdf").toFile().exists()));
        }
        catch (FileNotFoundException e)
        {
            logger.error("FileNotFoundException", e);
        }

        // Wait until Complete Sign is visible...
        $("#COMPLETE_MANUAL_DOCUMENT").waitUntil(Condition.visible, 15_000);

        ContractInfo contractInfo = openedContract.clickCompleteSign(documentName).clickComplete();
        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();

        logger.info("Assert update notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Hover CC and verify user...");
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("AL", "CC"));
        $$(".contract-header-users .user").filter(Condition.exactText("CC")).first().hover();
        $(".rc-tooltip-inner .spinner").waitUntil(Condition.disappear, 18_000);
        $(".contract-user__name").waitUntil(Condition.visible, 15_000).
                shouldHave(Condition.exactText( Const.PREDEFINED_CCN.getFirstName() + " " + Const.PREDEFINED_CCN.getLastName() ));
        $(".contract-user__status").waitUntil(Condition.visible, 15_000).
                shouldHave(Condition.exactText("Chief Negotiator"));

        Screenshoter.makeScreenshot();
    }
}
