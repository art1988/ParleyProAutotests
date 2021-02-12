package tests.classic.classic_runner;

import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DiscussionsOfSingleContract;
import pages.DocumentComparePreview;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import java.io.File;

@Listeners({ScreenShotOnFailListener.class})
public class UploadNewVersionFromCP
{
    private static Logger logger = Logger.getLogger(UploadNewVersionFromCP.class);

    @Test
    @Parameters({"documentName", "cpDocumentName"})
    public void uploadNewVersionFromCP(String documentName, String cpDocumentName)
    {
        String docNameWithoutExtension = documentName.substring(0, documentName.indexOf("."));

        OpenedContract openedContract = new OpenedContract();

        DocumentComparePreview comparePreview = openedContract.
                clickUploadNewVersionButton(documentName.substring(0, documentName.indexOf("."))).
                clickUploadCounterpartyDocument(new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + cpDocumentName),
                                                docNameWithoutExtension,
                                    "Classic contract - client docs");

        DiscussionsOfSingleContract discussionsOfSingleContract = comparePreview.clickUpload(true);
    }
}
