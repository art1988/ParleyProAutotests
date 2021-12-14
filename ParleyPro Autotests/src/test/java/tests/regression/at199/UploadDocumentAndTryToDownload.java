package tests.regression.at199;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocumentAndTryToDownload
{
    private String contractTitle = "AT-199 ctr";
    private static Logger logger = Logger.getLogger(UploadDocumentAndTryToDownload.class);


    @Test(priority = 1)
    public void uploadDocument()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle(contractTitle);
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        logger.info("Uploading D&B Hoovers - Order Agreement for.docx [wait up to 5 mins...]");
        new AddDocuments().clickUploadCounterpartyDocuments(Const.DOC_AT199_DOWNLOAD);
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 5);

        new ContractInNegotiation(contractTitle).clickOk();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void downloadTest() throws IOException, InterruptedException
    {
        logger.info("Trying to download... [wait up to 5 mins...]");
        new OpenedContract().clickDocumentActionsMenu("D&B Hoovers - Order Agreement for").clickDownload(false);

        $(".spinner").waitUntil(Condition.disappear, 60_000 * 5);

        Thread.sleep(3_000);

        logger.info("Assert that file was downloaded...");
        try (Stream<Path> stream = Files.list(Paths.get(Const.DOWNLOAD_DIR.getPath())))
        {
            Optional<String> matchingFile = stream.map(Path::getFileName)
                                                  .map(Path::toString)
                                                  .filter(file -> file.startsWith("D&B Hoovers"))
                                                  .findFirst();

            String downloadedFile = matchingFile.get();
            Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + downloadedFile).exists(), "Looks like that " + downloadedFile + " has not been downloaded !!!");
        }
        catch (NoSuchElementException e)
        {
            logger.error("Unable to download file !!!", e);
        }
    }

    @Test(priority = 3)
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
