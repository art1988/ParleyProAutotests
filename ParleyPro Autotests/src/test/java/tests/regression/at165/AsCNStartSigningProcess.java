package tests.regression.at165;

import com.codeborne.selenide.CollectionCondition;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AsCNStartSigningProcess
{
    private static Logger logger = Logger.getLogger(AsCNStartSigningProcess.class);
    private String contractName = "AT-165 SIGN CTR";

    @Test(priority = 1)
    public void loginAsCNAndMoveToSign() throws IOException, InterruptedException
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract(contractName);

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToSign("AT-14").clickStart();

        logger.info("Assert that file was downloaded...");
        Thread.sleep(4_000);

        try (Stream<Path> stream = Files.list(Paths.get(Const.DOWNLOAD_DIR.getPath())))
        {
            Optional<String> matchingFile = stream.map(Path::getFileName)
                                                  .map(Path::toString)
                                                  .filter(file -> file.startsWith("AT-14"))
                                                  .findFirst();

            String downloadedFile = matchingFile.get();
            Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + downloadedFile).exists(), "Looks like that " + downloadedFile + " has not been downloaded !!!");
        }

        logger.info("Making sure that status was changed to SIGN...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
