package tests.templates.at48;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContract
{
    private static Logger logger = Logger.getLogger(CreateContract.class);

    @Test(priority = 1)
    @Description("This test creates contract that doesn't match template")
    public void createContractThatDoesNotMatchTemplate()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("ContractThatDoesNotMatchTemplate_AT48");

        // Fields that doesn't match template
        contractInformation.setContractingRegion("region2");
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type2");

        contractInformation.setDueDate();
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.clickSave();

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickSelectTemplateTab();

        logger.info("Assert that template list is empty...");
        $(".documents-add__body").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("There are no published templates"));

        Screenshoter.makeScreenshot();

        addDocuments.clickUploadDocumentTab();
    }

    @Test(priority = 2)
    @Description("This test changes contract info so it match template")
    public void changeContractInfo()
    {
        ContractInformation contractInformation = new OpenedContract().clickContractInfo();

        logger.info("Setting necessary fields to match template...");

        contractInformation.setContractingRegion("region1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setContractingCountry("country1"); // set country again because backspace clears its field too
        contractInformation.clickSave();

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickSelectTemplateTab();

        logger.info("Assert that template item became available...");
        $$(".documents-add-templates-item").shouldHave(CollectionCondition.size(1)).get(0)
                .shouldHave(Condition.exactText("Template_AT48[ EDITED ]\nSome description of this template from autotest."));
    }
}
