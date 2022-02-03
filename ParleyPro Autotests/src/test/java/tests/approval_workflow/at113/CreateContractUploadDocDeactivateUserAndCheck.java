package tests.approval_workflow.at113;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.FieldType;
import forms.ConfirmApprovers;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class CreateContractUploadDocDeactivateUserAndCheck
{
    private static Logger logger = Logger.getLogger(CreateContractUploadDocDeactivateUserAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("at-113 contract title");
        contractInformation.setContractingRegion("region2");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity2");
        contractInformation.setContractingDepartment("department2");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type2");
        contractInformation.setValueForCustomField("Approve T1", FieldType.SELECT, "YES");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }

    @Test(priority = 2)
    public void clickApproval()
    {
        ConfirmApprovers confirmApprovers = new OpenedContract().switchDocumentToPreNegotiateApproval("AT-14");

        logger.info("Assert that T1 is in the list...");
        Assert.assertEquals(confirmApprovers.getListOfApprovers(), "T1\n2 members",
                "T1 is not in the list of Approvers !!!");

        Screenshoter.makeScreenshot();

        confirmApprovers.clickCancel();
    }

    @Test(priority = 3)
    public void deactivateUser()
    {
        new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab().switchEnableToggle("U1", false);

        logger.info("Assert that user was disabled...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"U1\")').parent().find(\".tumbler\").hasClass(\"checked\")"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void clickApprovalAndCheck()
    {
        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("at-113 contract title");

        ConfirmApprovers confirmApprovers = new OpenedContract().switchDocumentToPreNegotiateApproval("AT-14");

        logger.info("Assert that T1 is still in the list...");
        Assert.assertEquals(confirmApprovers.getListOfApprovers(), "T1\n1 member",
                "T1 is not in the list of Approvers !!!");

        Screenshoter.makeScreenshot();

        confirmApprovers.clickCancel();
    }
}
