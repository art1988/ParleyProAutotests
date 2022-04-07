package tests.basics.at228;

import com.codeborne.selenide.*;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AcceptTooltipShouldNotBeAvailable
{
    private SideBar sideBar;
    private CKEditorActive ckEditorActive;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(AcceptTooltipShouldNotBeAvailable.class);


    @BeforeMethod(description = "Add classic contract and Upload CP document")
    public void preSetup()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformationForm = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-228 Accept_Tooltip");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOC_AT166_ONE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        openedContract.switchDocumentToNegotiate("AT-166_Manufacturing Agreement_1", "CounterpartyAT", true).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        openedContract.clickUploadNewVersionButton("AT-166_Manufacturing Agreement_1")
                      .clickUploadCounterpartyDocument(Const.DOC_AT_228_NEW_VER_FROM_CP, "AT-166_Manufacturing Agreement_1", "AT-228 Accept_Tooltip")
                      .clickUpload(true)
                      .clickDocumentsTab();

        logger.info("Assure that 2 deletions were added and 3 insertions were added...");
        $$("del").shouldHave(CollectionCondition.size(2));
        $$("ins").shouldHave(CollectionCondition.size(3));
    }

    @Test
    public void acceptTooltipShouldNotBeAvailable() throws InterruptedException
    {
        checkRejectTooltip();
        rejectRedAndBlue();
        checkLastPostOfDiscussion();
        downloadDocAsCPAndUploadAgainAsCP();
    }

    @Step("Hover over the 'June 23, 2020' redline => only ‘Reject’ is available")
    public void checkRejectTooltip()
    {
        ckEditorActive = openedContract.clickByParagraph("This Manufacturing Agreement");
        $$(".cke_inner del").filterBy(Condition.text("June 23")).first().hover();
        $$(".ckeditor-tooltip span").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("Reject"));
        Screenshoter.makeScreenshot();
    }

    @Step("Reject red 'June 23, 2020' and blue 'February 14, 2022' and post as Queued")
    public void rejectRedAndBlue() throws InterruptedException
    {
        logger.info("Rejecting red 'June 23'...");
        $$(".cke_inner del").filterBy(Condition.text("June 23")).first().hover();
        $$(".ckeditor-tooltip span").filterBy(Condition.exactText("Reject")).first().click();
        Thread.sleep(1_000);

        logger.info("Rejecting blue 'February 14'...");
        $$(".cke_inner ins").filterBy(Condition.text("February 14")).first().hover();
        $$(".ckeditor-tooltip span").filterBy(Condition.exactText("Reject")).first().click();
        Thread.sleep(1_000);

        ckEditorActive.selectQueued().clickPost();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Queued post has been successfully created."));

        $$("ins").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("test", "123456"));
        $$("del").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("32746"));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"June 23, 2020\")').length === 1"), "There is no 'June 23, 2020' on page !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"February 14, 2022\")').length === 0"), "'February 14, 2022' is still on page, but shouldn't !!!");
    }

    @Step("Clicks on the discussion bubble and check the latest post")
    public void checkLastPostOfDiscussion()
    {
        logger.info("Open discussion...");
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIconSoft("This Manufacturing Agreement");

        SelenideElement lastPost = $$(".discussion2-post").shouldHave(CollectionCondition.size(2)).last();
        lastPost.find(".discussion2-post__text").findAll(By.tagName("ins")).shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("June 23, 2020"));
        lastPost.find(".discussion2-post__text").findAll(By.tagName("del")).shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("February 14, 2022"));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.discussion2-post').last().find('.discussion2-post__text span:contains(\"test\")').length === 1"), "There is no test text on last post !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.discussion2-post').last().find('.discussion2-post__text span:contains(\"32746\")').length === 0"), "32746 is still present on last post !!!");
        Screenshoter.makeScreenshot();
    }

    @Step("Download the document for CP and upload it back as CP")
    public void downloadDocAsCPAndUploadAgainAsCP() throws InterruptedException
    {
        try
        {
            openedContract.clickDocumentActionsMenu("AT-166_Manufacturing Agreement_1")
                          .clickDownload(true)
                          .clickDownloadForCounterparty();

            Thread.sleep(3_000);
            logger.info("Assert that file was downloaded...");

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                            until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "AT-166_Manufacturing Agreement_1.docx").toFile().exists()),
                    "Looks like that it is unable to download file as counterparty !!!");
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        logger.info("Delete previous uploaded document...");
        openedContract.clickDocumentActionsMenu("AT-166_Manufacturing Agreement_1").clickCancel().clickCancel();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been cancelled."));
        $(".cancelled").shouldBe(Condition.visible);
        openedContract.clickDocumentActionsMenu("AT-166_Manufacturing Agreement_1").clickDelete().clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been deleted."));

        Thread.sleep(2_000);
        sideBar.clickInProgressContracts(false).selectContract("AT-228 Accept_Tooltip");
        logger.info("Uploading just downloaded...");
        new AddDocuments().clickUploadCounterpartyDocuments(Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "AT-166_Manufacturing Agreement_1.docx").toFile());
        new ContractInNegotiation("AT-228 Accept_Tooltip").clickOk();

        $$("ins").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("June 23, 2020"));
        $$("del").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("February 14, 2022"));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document__body-content .discussion-indicator').closest('.document-paragraph__content:contains(\"test\")').length === 1"), "There is no 'test' on page !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document__body-content .discussion-indicator').closest('.document-paragraph__content:contains(\"123456\")').length === 1"), "There is no '123456' on page !!!");
        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void cleanDownloadsDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
