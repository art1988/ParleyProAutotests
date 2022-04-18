package tests.classic.at243;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import forms.UploadDocumentDetectedChanges;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.Discussions;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CanNotUploadNewVersionTest
{
    private final String CONTRACT_NAME = "AT-243 Liberty Mutual Can not upload new version";
    private SideBar sideBar;
    private OpenedContract openedContract;


    private static Logger logger = Logger.getLogger(CanNotUploadNewVersionTest.class);

    @Test
    public void canNotUploadNewVersionTest()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformationForm = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle(CONTRACT_NAME);
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOC_AT243_LM_1);
        new UploadDocumentDetectedChanges().clickOk();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        openedContract = new OpenedContract();

        logger.info("Check that amount of discussions is 13...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "13", "Amount of discussion is not equal 13 !!!");

        logger.info("Uploading new version...");
        Discussions discussions = openedContract.clickUploadNewVersionButton("Custodia MSA_v1")
                                                .clickUploadCounterpartyDocument(Const.DOC_AT243_LM_2, "Custodia MSA_v1", CONTRACT_NAME)
                                                .clickUpload(true);

        logger.info("Check that amount of discussions is 17...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "17", "Amount of discussion is not equal 17 !!!");
    }
}
