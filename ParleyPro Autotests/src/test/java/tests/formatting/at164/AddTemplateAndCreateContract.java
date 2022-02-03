package tests.formatting.at164;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.add.AddTemplates;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.TemplatesPage;
import pages.subelements.FieldsPanel;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddTemplateAndCreateContract
{
    private static Logger logger = Logger.getLogger(AddTemplateAndCreateContract.class);

    @Test(priority = 1)
    public void addTemplateAndPublishIt()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        AddTemplates addTemplates = templatesPage.clickNewTemplate();

        addTemplates.clickUploadTemplatesButton( Const.TEMPLATE_AT164 );
        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.exactText("Template Template_AT-164-Manufacturing_Agreement was added."));

        templatesPage.selectTemplate("Template_AT-164-Manufacturing_Agreement").clickPublishButton();
        $$(".template__status").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Published"));
    }

    @Test(priority = 2)
    public void addContractOutOfTemplate()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-164 // CTR - Document moves from Review to Draft");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickSelectTemplateTab().selectTemplate("Template_AT-164-Manufacturing_Agreement");
        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.exactText("Document Template_AT-164-Manufacturing_Agreement has been successfully uploaded."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void fillAllFields() throws InterruptedException
    {
        FieldsPanel fieldsPanel = new OpenedContract().getFieldsPanel();

        fieldsPanel.setValueForCustomField("Company name", "Counterparty");
        Thread.sleep(500);
        fieldsPanel.setValueForCustomField("Counterparty Address", "Address-1");
        Thread.sleep(500);
        fieldsPanel.setValueForCustomField("Delivery terms", "3");
        Thread.sleep(500);
        fieldsPanel.setValueForCustomField("Warranty period", "1");
        Thread.sleep(500);

        fieldsPanel.clickSaveButton();
        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.exactText("The fields have been saved successfully"));

        logger.info("Checking that fields were filled in document view...");
        $$("span[class='is-placeholder']").shouldHave(CollectionCondition.size(5)).shouldHave(CollectionCondition.exactTexts("Counterparty", "Address-1", "3", "1", "Counterparty"));

        Screenshoter.makeScreenshot();
    }
}
