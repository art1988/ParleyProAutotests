package tests.templates.at213;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.TemplatesPage;
import pages.subelements.SideBar;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddTemplateAndMakeDocument
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AddTemplateAndMakeDocument.class);


    @Test(priority = 1)
    public void addTemplateAndPublish()
    {
        sideBar = new DashboardPage().getSideBar();

        TemplatesPage templatesPage = sideBar.clickTemplates();
        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT213 );

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" was added."));
        Selenide.refresh();
        $$(".template__title").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("AT_213_Supply Agreement_DEBUG_"));

        templatesPage.selectTemplate("AT_213_Supply Agreement_DEBUG_").clickPublishButton();

        $$(".template__status").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible).shouldHave(Condition.exactText("Published"));
    }

    @Test(priority = 2)
    @Description("Final check of test happens here. Test verifies that document was successfully added from template.")
    public void makeDocumentOutOfTemplate()
    {
        ContractInformation contractInformationForm = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-213_Tag_NUM");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        new AddDocuments().clickSelectTemplateTab().selectTemplate("AT_213_Supply Agreement_DEBUG_");

        logger.info("Making sure that document was added...");

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
        $(".document__body-content").shouldBe(Condition.visible);
        Assert.assertTrue(Selenide.executeJavaScript("return ($('.document-paragraph__content-text:contains(\"SUPPLY AGREEMENT\")').length === 1) && " +
                "($('.document-paragraph__content-text:contains(\"SUPPLY AGREEMENT\")').length === 1)"));
        Screenshoter.makeScreenshot();
    }
}
