package tests.routing_workflow.at56;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.ContractInformation;
import io.qameta.allure.Description;
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
public class AsCNUploadDocAndApprove
{
    @Test(priority = 1)
    @Description("This test uploads doc and approves as USERCN.")
    public void asCNUploadDocAndApprove() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("CTR-AT56");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setValueForCustomField("Enable Routing", FieldType.SELECT, "Yes");
        contractInformation.setValueForCustomField("Enable Approval", FieldType.SELECT, "Yes");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.text("has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 45_000);

        OpenedContract openedContract = new OpenedContract();
        // move to Review
        openedContract.switchDocumentToReview("AT-14").clickStart();
        // move to Approval
        openedContract.switchDocumentToPreNegotiateApproval("AT-14").clickStartApproval();

        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("APPROVAL\n(1)"), 60_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("APPROVAL"), 60_000);
        $("#APPROVE_DOCUMENT").waitUntil(Condition.visible, 60_000);

        openedContract.clickApproveButton("AT-14").clickApproveButton();
        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.exactText("Document AT-14 has been approved"));

        $(".user-icon-checked").waitUntil(Condition.visible, 60_000);
        $$(".user-icon-checked").shouldHave(CollectionCondition.size(1)).get(0).parent().shouldHave(Condition.exactText("AL"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logout()
    {
        new DashboardPage().getSideBar().logout();
    }
}
