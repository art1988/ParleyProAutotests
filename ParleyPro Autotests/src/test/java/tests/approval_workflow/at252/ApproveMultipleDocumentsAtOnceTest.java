package tests.approval_workflow.at252;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ApproveDocument;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ApproveMultipleDocumentsAtOnceTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;
    private final String contractName = "AT-252: ApproveMultipleDocumentsAtOnceTest";
    private static Logger logger = Logger.getLogger(ApproveMultipleDocumentsAtOnceTest.class);


    @BeforeMethod
    public void createContractAndUploadDocs()
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

        new AddDocuments().clickUploadMyTeamDocumentsMultiple(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE, Const.DOCUMENT_FORMATTING_SAMPLE});

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(2)", "DRAFT", "DRAFT"));

        openedContract = new OpenedContract();
    }


    @Test
    public void approveMultipleDocumentsAtOnceTest()
    {
        moveContractToPreNegotiateApproval();
        asApprover1ApproveFirstDoc();
    }

    @Step
    public void moveContractToPreNegotiateApproval()
    {
        openedContract.switchContractToPreNegotiateApproval().clickNext().clickStartApproval();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(2)", "APPROVAL", "APPROVAL"));
    }

    @Step
    public void asApprover1ApproveFirstDoc()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_APPROVER_USER_1.getEmail());
        loginPage.setPassword(Const.PREDEFINED_APPROVER_USER_1.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(contractName);
        openedContract = new OpenedContract();

        Selenide.sleep(2_000);
        // click by Approve button for Formatting document
        Selenide.executeJavaScript("$('.rename.document__header-rename:contains(\"Forma\")').closest('.document__header-row').find('#APPROVE_DOCUMENT').click()");
        new ApproveDocument("Formatting").markDocument("AT-14").clickApproveButton();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Documents have been approved"));

        Selenide.sleep(8_000);
    }
}
