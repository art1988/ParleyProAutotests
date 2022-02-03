package tests.requests.at185;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddRequestAndSubmit
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(AddRequestAndSubmit.class);

    @Test(priority = 1)
    public void addRequestAndSubmit() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        // Login as REQUESTER
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("Request for at-185");
        contractRequest.selectValueForField("ReqField_1_AT185_routing", "f1_v1");
        contractRequest.selectValueForField("ReqField_2_AT185_approval", "f2_v2");
        contractRequest.clickUploadMyTeamDocumentsWithDetectedChanges(new File[]{Const.DOC_1_AT185})
                       .setCounterpartyOrganization("CounterpartyAT")
                       .setCounterpartyNegotiatorEmail("arthur.khasanov+cpat@parleypro.com")
                       .clickOk();

        Thread.sleep(1_000);

        logger.info("Making sure that Counterparty organization and Counterparty Chief Negotiator fields are filled in with the values selected before and document was added...");
        Assert.assertEquals($("#counterpartyorganization").getValue(), "CounterpartyAT", "Counterparty organization field wasn't filled !!!");
        Assert.assertEquals($("#counterpartychiefnegotiator").getValue(), "arthur.khasanov+cpat@parleypro.com", "Counterparty Chief Negotiator field wasn't filled !!!");
        $$(".upload-field__file").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.texts("AT185-Manufacturing Agreement-redlines-D1.docx"));

        logger.info("Uploading Doc2 as my team...");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOC_2_AT185});

        logger.info("Assert that files are 2...");
        $$(".upload-field__file").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.texts("AT185-Manufacturing Agreement-redlines-D1.docx", "AT185-Lists-D2.docx"));

        contractRequest.clickSubmitRequest();

        $(".notification-stack").shouldHave(Condition.text("Your contract request has been submitted."));
    }

    @Test(priority = 2)
    public void logoutRequester()
    {
        dashboardPage.getSideBar().logout();
    }
}
