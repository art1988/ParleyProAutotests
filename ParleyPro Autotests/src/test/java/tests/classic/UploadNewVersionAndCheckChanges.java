package tests.classic;

import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DocumentComparePreview;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

@Listeners({ ScreenShotOnFailListener.class})
public class UploadNewVersionAndCheckChanges
{
    private String documentName = "AT-40";
    private String contractName = "AT40 Classic contract";

    private static Logger logger = Logger.getLogger(UploadNewVersionAndCheckChanges.class);

    @Test
    @Description("This test uploads new version and verify changes")
    public void uploadNewVersionAndCheckChanges()
    {
        OpenedContract openedContract = new OpenedContract();

        DocumentComparePreview comparePreview = openedContract.clickUploadNewVersionButton(documentName).
                clickUploadCounterpartyDocument(Const.CONTRACT_CLASSIC_AT40_2, documentName, contractName);

        // TODO: add check of added comments after fixing of PAR-12858
        logger.info("Assert that counters are correct...");
        Assert.assertEquals(comparePreview.getCounterAdded(), "2");
        Assert.assertEquals(comparePreview.getCounterEdited(), "2");
        Assert.assertEquals(comparePreview.getCounterDeleted(), "1");

        logger.info("Assert that icons are correct for corresponded paragraphs");
        StringBuffer jsCode = new StringBuffer("var ar = $('.update-document__changes.ui-td .icon');");
        jsCode.append("var string = \"\";");
        jsCode.append("for( var i = 0; i < ar.length; i++ ) { string += ar[i].getAttribute(\"value\"); } ");
        jsCode.append("return string;");
        Assert.assertEquals(Selenide.executeJavaScript(jsCode.toString()), "deletedaddedaddededitededited");

        comparePreview.clickUpload();
    }
}
