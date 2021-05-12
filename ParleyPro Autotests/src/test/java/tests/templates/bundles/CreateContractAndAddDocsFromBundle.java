package tests.templates.bundles;

import com.codeborne.selenide.CollectionCondition;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndAddDocsFromBundle
{
    private static Logger logger = Logger.getLogger(CreateContractAndAddDocsFromBundle.class);

    @Test(priority = 1)
    public void createContractWithUnmatchedSettingsForBundle()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("Unmatched settings for bundle");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setContractingRegion("region2");
        contractInformation.setContractingCountry("country2");
        contractInformation.setContractEntity("entity2");
        contractInformation.setContractingDepartment("department2");
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type2");
        contractInformation.clickSave();

        AddDocuments addDocuments = new AddDocuments().clickSelectTemplateTab();

        logger.info("Assert that there is NO bundle in the list...");
        $$(".documents-add-templates-item__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Template_AT-86_text_cut_off", "Template_AT-77_dummy", "Template_AT48"));

        // Changing contract info
        contractInformation = new OpenedContract().clickContractInfo();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        logger.info("Assert that bundle IS NOW AVAILABLE in the list...");
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("Unmatched settings for bundle");
        addDocuments = new AddDocuments().clickSelectTemplateTab();

        $$(".documents-add-templates-item__title").shouldHave(CollectionCondition.size(4))
                .shouldHave(CollectionCondition.textsInAnyOrder("Template_AT-86_text_cut_off", "Template_AT-77_dummy", "Template_AT48", "TEST Bundle AKM"));

        Screenshoter.makeScreenshot();

        // Add docs from bundle
        OpenedContract openedContract = addDocuments.selectTemplate("TEST Bundle AKM");

        System.out.println("ORDER = " + Saver.getTemplates());
    }
}
