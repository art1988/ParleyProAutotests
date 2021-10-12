package tests.classic.classic_runner;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementShould;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.DiscussionsOfSingleContract;
import pages.DocumentComparePreview;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadNewVersionFromCP
{
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(UploadNewVersionFromCP.class);

    @Test
    @Parameters({"documentName", "cpDocumentName"})
    public void uploadNewVersionFromCP(String documentName, String cpDocumentName)
    {
        String docNameWithoutExtension = documentName.substring(0, documentName.indexOf("."));

        OpenedContract openedContract = new OpenedContract();

        DocumentComparePreview comparePreview = null;
        try
        {
            comparePreview = openedContract.
                    clickUploadNewVersionButton(documentName.substring(0, documentName.indexOf("."))).
                    clickUploadCounterpartyDocument(new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + cpDocumentName),
                            docNameWithoutExtension,
                            "Classic contract - client docs");

            logger.info("Waiting until spinner will disappear [up to 5 minutes]...");
            $(".spinner-backdrop .spinner").waitUntil(Condition.disappear, 60_000 * 5);
        }
        catch (ElementShould elementShouldException)
        {
            // handle of 500 http code case like in PAR-14783
            logger.error("Exception happened: " + elementShouldException.getMessage());
            logger.error("Looks like that NPE happened (500 http code) and it is unable to Upload document !!!");
            softAssert.fail("Looks like that NPE (500 http code) happened and it is unable to Upload document !!!", elementShouldException);

            logger.info("Making screenshot before refresh...");
            Screenshoter.makeScreenshot();

            logger.info("Force to refresh page...");
            Selenide.refresh();

            softAssert.assertAll();
            return;
        }

        DiscussionsOfSingleContract discussionsOfSingleContract = comparePreview.clickUpload(true);
    }
}
