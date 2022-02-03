package tests.templates.bundles.at114;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CreateContractAndAddDocsFromBundle
{
    private static Logger logger = Logger.getLogger(CreateContractAndAddDocsFromBundle.class);

    @Test(priority = 1)
    public void createContractWithUnmatchedSettingsForBundle() throws InterruptedException
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
        Thread.sleep(4_000);
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

        // Add docs from bundle by selecting this bundle
        OpenedContract openedContract = addDocuments.selectTemplate("TEST Bundle AKM");

        $$(".notification-stack .notification_type_success").shouldHave(CollectionCondition.size(3), 20_000);
    }

    @Test(priority = 2, enabled = false)
    public void checkOrder()
    {
        List<String> initialOrder = Saver.getTemplates(),
                     currentOrder = new ArrayList<>(3);

        for( int i = 0; i < $$(".document__header-rename").size(); i++ )
        {
            currentOrder.add($$(".document__header-rename").get(i).getText());
        }
        logger.info("Current order is: " + currentOrder);

        Assert.assertEquals(currentOrder, initialOrder, "Order of added docs is wrong !!!");

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 3)
    public void removeJustAddedDocs()
    {
        List<String> docs = Saver.getTemplates();

        for( int i = 0; i < docs.size(); i++ )
        {
            new OpenedContract().clickDocumentActionsMenu(docs.get(i)).clickDelete().clickDelete();

            $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("has been deleted"));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);

            new DashboardPage().getSideBar()
                               .clickInProgressContracts(false)
                               .selectContract("Unmatched settings for bundle");
        }

        Screenshoter.makeScreenshot();
    }
}
