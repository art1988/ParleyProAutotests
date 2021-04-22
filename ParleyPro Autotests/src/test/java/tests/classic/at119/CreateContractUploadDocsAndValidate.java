package tests.classic.at119;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocsAndValidate
{
    private static Logger logger = Logger.getLogger(CreateContractUploadDocsAndValidate.class);

    @Test(priority = 1)
    public void createContractAndUploadDocV1()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("AT-119 CTR");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.CLASSIC_AT_119_V1 );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document PAR-13996_V1 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        new ContractInNegotiation("AT-119 CTR").clickOk();

        logger.info("Making sure that 3 internal discussions have been created...");

        Assert.assertEquals(new OpenedContract().getAmountOfContractDiscussion(), "3", "Total amount of discussions is wrong !!!");
        $$(".discussion-indicator").shouldHave(CollectionCondition.size(5)); // on contract header, document header and 3 inside document
        for( int n = 0; n < $$(".discussion-indicator").size(); n++ )
        {
            Assert.assertEquals($$(".discussion-indicator").get(n).getCssValue("color"), "rgba(25, 190, 155, 1)",
                    "The color of discussion icon for internal is wrong !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void makePostsQueued()
    {
        new OpenedContract().clickManageDiscussions()
                            .makeQueuedAllInternalDiscussions()
                            .confirmMakeQueued()
                            .clickDone();

        logger.info("Assert that '3 QUEUED' pink label was shown...");
        $(".label.label_theme_pink").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("3 QUEUED"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void downloadAsCounterparty()
    {
        try
        {
            new OpenedContract().clickDocumentActionsMenu("PAR-13996_V1")
                                .clickDownload(true)
                                .clickDownloadForCounterparty();

            logger.info("Assert that file was downloaded [ counterparty ]...");
            Thread.sleep(10_000);

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "PAR-13996_V1.docx").toFile().exists()));
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        Selenide.refresh();
        new OpenedContract();

        logger.info("Assert that all Posts become External...");
        for( int n = 0; n < $$(".discussion-indicator").size(); n++ )
        {
            Assert.assertEquals($$(".discussion-indicator").get(n).getCssValue("color"), "rgba(127, 111, 207, 1)",
                    "The color of discussion icon for External is wrong !!!");
        }

        logger.info("Assert that pink label disappeared...");
        $(".label.label_theme_pink").shouldBe(Condition.disappear);

        Screenshoter.makeScreenshot();
    }
}
