package tests.requests.at174;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;


public class CreateContractUploadDocAndCheck
{
    private static Logger logger = Logger.getLogger(CreateContractUploadDocAndCheck.class);

    @Test
    public void createContractUploadDocAndCheck() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("CTR for at-174");
        contractInformation.setContractValue("5002");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_LIFECYCLE_SAMPLE );

        new OpenedContract().switchDocumentToReview("pramata").clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("REVIEW\n(1)"), 40_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("REVIEW"), 40_000);

        logger.info("Checking that users weren't triggered...");
        Thread.sleep(4_000); // sleep for 4 sec

        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("AL"));
        $$(".document__score .user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("AL"));

        Screenshoter.makeScreenshot();
    }
}
