package tests.customer_params.at229;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
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
public class CheckClause
{
    private SideBar sideBar;


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
        CKEditorActive ckEditorActive = new OpenedContract().clickByParagraph("Test clauses");
        ckEditorActive.clickClauseButton().acceptClause("title");
        $$(".cke_inner del").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Test clauses"));
        $$(".cke_inner ins").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("This is clause text"));
        ckEditorActive.clickPost();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("has been successfully created."));
        $$("del").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Test clauses"));
        $$("ins").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("This is clause text"));
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }
}
