package tests.templates.at86;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndSelectTemplate
{
    private static Logger logger = Logger.getLogger(CreateContractAndSelectTemplate.class);

    @Test(priority = 1)
    public void createContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Template verification at-86");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();
    }

    @Test(priority = 2)
    @Description("This test selects template downloaded_Modified and checks that necessary text is present.")
    public void selectTemplateAndValidateText()
    {
        new AddDocuments().clickSelectTemplateTab()
                          .selectTemplate("downloaded_Modified");

        logger.info("Assert that notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 60_000).shouldHave(Condition.exactText("Document downloaded_Modified has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 75_000);

        logger.info("Assert that necessary text is present...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"“Alternative Base Rate\")').length === 1 && " +
                                                            "$('.document-paragraph__content-text:contains(\"“Benchmark Conforming Changes\")').length === 1 && " +
                                                            "$('.document-paragraph__content-text:contains(\"“Benchmark Replacement Event\")').length  === 1"),
                "Looks like that “Alternative Base Rate“ or “Benchmark Conforming Changes“ or “Benchmark Replacement Event“ is not present on page !!!");

        Screenshoter.makeScreenshot();
    }
}
