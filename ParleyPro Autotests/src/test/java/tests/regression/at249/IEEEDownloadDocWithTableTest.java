package tests.regression.at249;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import java.io.File;
import java.io.FileNotFoundException;
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
public class IEEEDownloadDocWithTableTest
{
    private static final String CONTRACT_NAME = "AT-249: IEEE_docWithTableDownloadTest";

    private static Logger logger = Logger.getLogger(IEEEDownloadDocWithTableTest.class);


    @Test
    public void ieeeDownloadDocWithTableTest() throws FileNotFoundException, InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(CONTRACT_NAME);
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments(Const.DOC_AT249_DOWNLOAD);
        new ContractInNegotiation(CONTRACT_NAME).clickOk();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Trying to download document...");
        new OpenedContract().clickDocumentActionsMenu("State Sponsorship Agreement - IEEE ICSE 2023_FINAL_short").clickDownload(false);
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);

        Thread.sleep(2_000);

        logger.info("Assert that file was downloaded...");
        try (Stream<Path> stream = Files.list(Paths.get(Const.DOWNLOAD_DIR.getPath())))
        {
            Optional<String> matchingFile = stream.map(Path::getFileName)
                                                  .map(Path::toString)
                                                  .filter(file -> file.startsWith("State Sponsorship Agreement - IEEE"))
                                                  .findFirst();

            String downloadedFile = matchingFile.get();
            Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + downloadedFile).exists(), "Looks like that " + downloadedFile + " has not been downloaded !!!");
        }
        catch (NoSuchElementException | IOException e)
        {
            logger.error("Unable to download document !!!", e);
            Assert.fail("Unable to download document !!!");
        }
    }

    @AfterMethod
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
