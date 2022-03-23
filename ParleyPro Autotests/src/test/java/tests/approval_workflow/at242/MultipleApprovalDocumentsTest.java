package tests.approval_workflow.at242;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.AboutToStartApproval;
import forms.ApproveDocument;
import forms.ConfirmApprovers;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MultipleApprovalDocumentsTest
{
    private final String contractName = "AT-242: multiple documents approval contract";
    public static final Path MULTIPLE_DOCS_DIR = Paths.get(System.getProperty("user.dir") + "/Documents/AT242");

    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(MultipleApprovalDocumentsTest.class);


    @BeforeMethod
    public void createContractAndUploadDocs() throws IOException
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractName);
        contractInformation.setContractValue("250000");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        File[] d1d2d3FilesToUpload = Files.list(MULTIPLE_DOCS_DIR).map(Path::toAbsolutePath).map(Path::toFile).toArray(File[]::new);
        new AddDocuments().clickUploadMyTeamDocumentsMultiple(d1d2d3FilesToUpload);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(3)", "DRAFT", "DRAFT", "DRAFT"));

        openedContract = new OpenedContract();
    }

    @Test
    public void approveTest()
    {
        moveContractToApproveWithoutD2();
        moveD2ToApproveAndApproveIt();
        loginAsApproverAndApproveD1AndD3();
        loginBackAsCNAndMakePreSignApprove();
    }

    @Step
    public void moveContractToApproveWithoutD2()
    {
        AboutToStartApproval aboutToStartApproval = openedContract.switchContractToPreNegotiateApproval();

        $$(".checkbox.with_label").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("D1", "D2", "D3"));
        aboutToStartApproval.checkDocument("D2").clickNext().clickStartApproval();

        logger.info("Check that D1 and D3 have been moved to the Approval stage. Approver is assigned to the documents. D2 is still in the Draft stage...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D1")).first().closest(".document__header").find(".lifecycle").shouldHave(Condition.exactText("APPROVAL"));
        $$(".document__header-rename > span").filterBy(Condition.exactText("D2")).first().closest(".document__header").find(".lifecycle").shouldHave(Condition.exactText("DRAFT"));
        $$(".document__header-rename > span").filterBy(Condition.exactText("D3")).first().closest(".document__header").find(".lifecycle").shouldHave(Condition.exactText("APPROVAL"));

        $$(".document__header-rename > span").filterBy(Condition.exactText("D1")).first().closest(".document__header").find(".user").shouldHave(Condition.exactText("Ar"));
        $$(".document__header-rename > span").filterBy(Condition.exactText("D2")).first().closest(".document__header").find(".user").shouldHave(Condition.exactText("AL"));
        $$(".document__header-rename > span").filterBy(Condition.exactText("D3")).first().closest(".document__header").find(".user").shouldHave(Condition.exactText("Ar"));
    }

    @Step
    public void moveD2ToApproveAndApproveIt()
    {
        AboutToStartApproval aboutToStartApproval = openedContract.switchDocumentToPreNegotiateApproval("D2");

        $$(".checkbox.with_label").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.textsInAnyOrder("D2"));

        ConfirmApprovers confirmApprovers = aboutToStartApproval.clickNext();
        confirmApprovers.deleteApprover(Const.PREDEFINED_APPROVER_USER_1.getFirstName());
        confirmApprovers.addParticipant(Const.PREDEFINED_USER_CN_ROLE.getFirstName());
        confirmApprovers.clickStartApproval();

        logger.info("Check that D2 has been moved to Approval...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D2")).first().closest(".document__header").find(".lifecycle").shouldHave(Condition.exactText("APPROVAL"));

        logger.info("Check that Approve, Reject and Cancel approval buttons are available for D2...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D2")).first().closest(".document__header-row").findAll("button[id]").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.textsInAnyOrder("Approve", "Reject", "Cancel approval", ""));
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(3)", "APPROVAL", "APPROVAL", "APPROVAL"));

        openedContract.clickApproveButton("D2").clickApproveButton();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Document D2 has been approved"));

        logger.info("Wait until user AL with checkmark become visible for D2...");
        $$(".user-icon-checked").shouldHave(CollectionCondition.size(1)).first().parent().shouldHave(Condition.exactText("AL")); // checkmark for user icon become visible

        logger.info("Check that Approve and Reject buttons disappear for D2...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D2")).first().closest(".document__header-row").findAll("button[id]").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Cancel approval", ""));

        Screenshoter.makeScreenshot();
    }

    @Step
    public void loginAsApproverAndApproveD1AndD3()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_APPROVER_USER_1.getEmail());
        loginPage.setPassword(Const.PREDEFINED_APPROVER_USER_1.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(contractName);
        openedContract = new OpenedContract();

        logger.info("Check that only D1 and D3 are in list...");
        $$(".document .document__header-rename > span").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("D1", "D3"));

        logger.info("Check that buttons Approve and Reject are available...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D1")).first().closest(".document__header-row").findAll("button[id]").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("Approve", "Reject", ""));
        $$(".document__header-rename > span").filterBy(Condition.exactText("D3")).first().closest(".document__header-row").findAll("button[id]").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("Approve", "Reject", ""));

        logger.info("Approve D1...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D1")).first().closest(".document__header-row").findAll("button[id]").filterBy(Condition.text("Approve")).first().click();
        new ApproveDocument("D1").clickApproveButton();

        logger.info("Approve D3...");
        $$(".document__header-rename > span").filterBy(Condition.exactText("D3")).first().closest(".document__header-row").findAll("button[id]").filterBy(Condition.text("Approve")).first().click();
        new ApproveDocument("D3").clickApproveButton();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(2)", "APPROVAL", "APPROVAL"));
        $("#APPROVE_DOCUMENT").shouldNotBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Step
    public void loginBackAsCNAndMakePreSignApprove()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(contractName);
        openedContract = new OpenedContract();

        // D1
        AboutToStartApproval aboutToStartApproval = openedContract.switchDocumentToPreSignApproval("D1");
        $$(".checkbox.with_label").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("D1", "D2", "D3"));

        logger.info("Check that only D1 is selected...");
        $$(".checkbox.with_label input[checked]").shouldHave(CollectionCondition.size(1)).first().parent().find("span").shouldHave(Condition.exactText("D1"));

        aboutToStartApproval.clickNext().clickStartApproval();

        logger.info("Check that only D1 was switched to Pre-sign Approval...");
        $$(".documents__list-content .lifecycle__item.active.approval").shouldHave(CollectionCondition.size(1)).first().closest(".document__header").find("span[title]").shouldHave(Condition.exactText("D1"));
        ////////


        // D2
        aboutToStartApproval = openedContract.switchDocumentToPreSignApproval("D2");
        $$(".checkbox.with_label").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("D2", "D3"));

        logger.info("Check that only D2 is selected...");
        $$(".checkbox.with_label input[checked]").shouldHave(CollectionCondition.size(1)).first().parent().find("span").shouldHave(Condition.exactText("D2"));
        aboutToStartApproval.checkDocument("D3").clickNext().clickStartApproval();

        logger.info("Check that all 3 documents were switched to Pre-Sign Approval...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(3)", "APPROVAL", "APPROVAL", "APPROVAL"));

        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void removeWorkflow()
    {
        String wrkflowName = "AT-241 workflow";

        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu(wrkflowName).clickDelete().clickDelete();
        $(byText(wrkflowName)).shouldNotBe(Condition.visible);
    }
}
