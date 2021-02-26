package tests.templates.at86;

import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;

import static com.codeborne.selenide.Selenide.$;

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
    @Description("This test selects template Template_AT-86_text_cut_off_2.")
    public void selectTemplate()
    {
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickSelectTemplateTab().selectTemplate("Template_AT-86_text_cut_off_2");

        logger.info("Assert that notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document Template_AT-86_text_cut_off_2 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }

    // TODO: continue after fixing of PAR-13624
}
