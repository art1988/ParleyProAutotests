package tests.regression.at162;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import forms.workflows.ApprovalWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddWorkflowUploadDocAndInviteCCN
{
    private String contractTitle = "AT-162 Contract";

    @Test(priority = 1)
    public void addWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = new DashboardPage().getSideBar()
                                                               .clickAdministration()
                                                               .clickWorkflowsTab()
                                                               .clickAddNewWorkflow()
                                                               .clickApproval(false);

        approvalWorkflow.setName("Prior to Sign");
        approvalWorkflow.clickPriorToSign(); // Just click Prior to Sign. No need to add participant
        approvalWorkflow.clickSave();

        $$(".workflows-list__cell.type_name").shouldHave(CollectionCondition.exactTexts("Name", "Prior to Sign"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void createContractAndUploadCPDoc()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle(contractTitle);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        new ContractInNegotiation(contractTitle).clickOk();

        Screenshoter.makeScreenshot();

        $(".notification-stack").should(Condition.appear);
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }

    @Test(priority = 3)
    public void sendInviteToCCN() throws InterruptedException
    {
        new OpenedContract().clickSendInvite()
                            .clickNext(false)
                            .setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() )
                            .clickStart(); // SEND

        $(".notification-stack").shouldHave(Condition.text("Documents have been shared with the Counterparty"));

        Screenshoter.makeScreenshot();
    }
}
