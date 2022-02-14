package tests.basics.at228;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AcceptTooltipShouldNotBeAvailable
{
    private SideBar sideBar;
    private CKEditorActive ckEditorActive;

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

        OpenedContract openedContract = new OpenedContract();
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
    }

    @Step("Hover over the 'June 23, 2020' redline => only ‘Reject’ is available")
    public void checkRejectTooltip()
    {
        ckEditorActive = new OpenedContract().clickByParagraph("This Manufacturing Agreement");
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
    }
}
