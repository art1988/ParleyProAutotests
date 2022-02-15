package tests.customer_params.at229;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckClause
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(CheckClause.class);


    @BeforeMethod(description = "Adds new contract, Uploads CP document")
    public void preSetup()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformationForm = sideBar.clickInProgressContracts(true).clickNewContractButton();
        contractInformationForm.setContractTitle("AT-229_Clause_CTR");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOC_AT229_CLAUSE );
        new ContractInNegotiation("AT-229_Clause_CTR").clickOk();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }

    @Test
    public void checkClause()
    {

    }
}
