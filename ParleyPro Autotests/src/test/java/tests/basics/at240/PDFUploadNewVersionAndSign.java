package tests.basics.at240;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PDFUploadNewVersionAndSign
{
    private SideBar sideBar;
    private OpenedContract openedContract;
    private Logger logger = Logger.getLogger(PDFUploadNewVersionAndSign.class);

    private String contractName = "AT-240: PDF upload new version and sign";
    private static final File PDF_DOC_CP      = new File(Const.PDF_DOCS_DIR + "/sample.pdf");
    private static final File PDF_DOC_NEW_VER = new File(Const.PDF_DOCS_DIR + "/At a Glance.pdf");


    @BeforeMethod
    public void createContractAndUploadPDF()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractName);
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments(PDF_DOC_CP);
        new ContractInNegotiation(contractName).clickOk();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Check some text from uploaded PDF doc...");
        $(withText("A Simple PDF File")).shouldBe(Condition.visible);
        $(withText("And more text.")).shouldBe(Condition.visible);
        $(withText("The end, and just as well.")).shouldBe(Condition.visible);
    }

    @Test
    public void uploadNewVersionOfPDF() throws InterruptedException, IOException
    {
        addInternalDiscussion();
        uploadNewVersion();
        signAndCompleteSign();
        checkDiscussionBoard();
    }

    @Step
    public void addInternalDiscussion() throws InterruptedException
    {
        logger.info("Add internal discussion...");

        String commentToAdd = "Initiate discussion - pdf";
        openedContract = new OpenedContract();
        OpenedDiscussionPDF openedDiscussionPDF = openedContract.clickStartDiscussionForPDF();
        openedDiscussionPDF.clickAddComment().setCommentForPDFDiscussion(commentToAdd).clickPost();
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(1))
                .first().find(".documents-pdf-discussion-post__comment").shouldHave(Condition.text(commentToAdd));
    }

    @Step
    public void uploadNewVersion()
    {
        String documentName = "sample";

        openedContract.clickUploadNewVersionButton(documentName).clickUploadCounterpartyDocumentPDF(PDF_DOC_NEW_VER, documentName, contractName);

        logger.info("Check that internal discussion is still open...");
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(1))
                .first().find(".documents-pdf-discussion-post__comment").shouldHave(Condition.text("Initiate discussion - pdf"));

        logger.info("Check some text of PDF doc after uploading of new version...");
        $(withText("Adobe")).shouldBe(Condition.visible);
        $(withText("ImageReady")).shouldBe(Condition.visible);
        $(withText("At A Glance")).shouldBe(Condition.visible);
        $(withText("digital imaging")).shouldBe(Condition.visible);
        $(withText("Canon")).shouldBe(Condition.visible);
    }

    @Step
    public void signAndCompleteSign() throws IOException, InterruptedException
    {
        openedContract.switchDocumentToSign("sample", false).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));

        logger.info("Assert that file was downloaded...");
        Thread.sleep(4_000);

        try (Stream<Path> stream = Files.list(Paths.get(Const.DOWNLOAD_DIR.getPath())))
        {
            Optional<String> matchingFile = stream.map(Path::getFileName)
                                                  .map(Path::toString)
                                                  .filter(file -> file.startsWith("sample"))
                                                  .findFirst();

            String downloadedFile = matchingFile.get();
            Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + downloadedFile).exists(), "Looks like that " + downloadedFile + " has not been downloaded !!!");
        }

        logger.info("Check presence of 'Complete sign' and 'Void sign' buttons...");
        $("#COMPLETE_MANUAL_DOCUMENT").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.exactText("Complete sign"));
        $("#VOID_DOCUMENT").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.exactText("Void sign"));

        ContractInfo contractInfo = openedContract.clickCompleteSign("sample").clickComplete();
        $$(".lifecycle__item.last").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("MANAGED", "SIGN"));

        Screenshoter.makeScreenshot();
    }

    @Step
    public void checkDiscussionBoard() throws InterruptedException
    {
        Discussions discussions = openedContract.clickByDiscussions();

        $$(".discussion2-header__row").shouldHave(CollectionCondition.size(1));
        $$(".discussion-indicator__count").shouldHave(CollectionCondition.size(2))
                .forEach(indicator -> Assert.assertEquals(indicator.getText(), "1", "Looks like that number of discussions is wrong !!!"));
        $$(".discussion2-label__status").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("INTERNAL"));

        Screenshoter.makeScreenshot();

        sideBar.clickAdministration();
        Thread.sleep(2_000);
    }

    @AfterMethod
    public void cleanDownloadsDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
