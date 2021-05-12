package tests.regression.at46;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateTemplateAndDocumentAndCheckNumber
{
    private String templateName = "Template_Regression_AT_46";
    private static Logger logger = Logger.getLogger(CreateTemplateAndDocumentAndCheckNumber.class);

    @Test(priority = 1)
    public void createTemplate()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);
        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.REGRESSION_TEMPLATE_AT46 );

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.exactText("Template Template_Regression_AT_46 was added."));
        $(".notification-stack .notification__close").click();

        $(".spinner").waitUntil(Condition.disappear, 30_000);

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate(templateName);
        editTemplatePage.clickPublishButton();

        logger.info("Making sure that status was changed to Published...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"" + templateName + "\")').next().text()"), "Published");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void createContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("3Q Numbering Contract based on template");

        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickSelectTemplateTab();
        addDocuments.selectTemplate(templateName);

        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.exactText("Document " + templateName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void checkNumberingAfterGMP()
    {
        logger.info("Scroll to paragraph...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"GCP:\")').parent().parent().next().find(\"h3\")[0].scrollIntoView({});");

        logger.info("Assert that first item after GMP starts with (a)...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"GMP:\")').parent().parent().next().find(\"h3\").text().trim().startsWith(\"(a)\")"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void deleteTemplate()
    {
        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickActionMenuTemplate(templateName)
                           .clickDelete()
                           .clickDelete();

        logger.info("Assert that delete template notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 35_000).shouldHave(Condition.exactText("Template " + templateName + " has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 45_000);
    }
}
