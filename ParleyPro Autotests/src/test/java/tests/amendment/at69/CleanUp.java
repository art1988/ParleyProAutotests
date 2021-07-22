package tests.amendment.at69;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    public void removeFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Removing 2 contract categories...");
        Selenide.executeJavaScript("$('input[value=\"Contracting department\"]')[0].scrollIntoView({})");
        contractFields.clickEditValues("Contract category");
        contractFields.removeValue("Contract category", "AmendmentTestcat");
        contractFields.removeValue("Contract category", "CatTest");

        logger.info("Removing Field1 - Field10...");
        for( int i = 1; i <= 10; i++ )
        {
            contractFields.removeField("Field" + i).clickDelete();
        }

        logger.info("Removing AmendFld1 - AmendFld5...");
        for( int i = 1; i <=5 ; i++ )
        {
            contractFields.removeField("AmendFld" + i).clickDelete();
        }

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Assert that only necessary fields are remain...");
        Assert.assertEquals((long) Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Summary\")').parent().find(\".input\").length"), 8,
                "Looks like that some fields weren't deleted !!!");
    }

    @Test(priority = 2)
    public void removeContracts()
    {
        logger.info("Removing contract from in-progress...");

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("at-69 Amendment-A");

        new OpenedContract(true).clickContractActionsMenu()
                                                .clickDeleteContract()
                                                .clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("has been deleted."));
        Screenshoter.makeScreenshot();


        logger.info("Removing contract from executed...");

        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("at-69 Amendment");

        new OpenedContract(true).clickContractActionsMenu()
                                                .clickDeleteContract()
                                                .clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("has been deleted."));
        Screenshoter.makeScreenshot();
    }
}
