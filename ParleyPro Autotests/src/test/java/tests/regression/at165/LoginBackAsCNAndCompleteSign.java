package tests.regression.at165;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsCNAndCompleteSign
{
    private String contractName = "AT-165 SIGN CTR";
    private static Logger logger = Logger.getLogger(LoginBackAsCNAndCompleteSign.class);

    @Test(priority = 1)
    public void loginBackAsCNAndCompleteSign()
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract(contractName);

        ContractInfo contractInfo = new OpenedContract().clickCompleteSign("AT-14").clickComplete();

        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();

        logger.info("Checking that contract status became MANAGED...");
        $(".lifecycle").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("MANAGED"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void cleanDownloadsDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
