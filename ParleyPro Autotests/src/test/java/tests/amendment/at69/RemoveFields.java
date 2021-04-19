package tests.amendment.at69;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveFields
{
    private Logger logger = Logger.getLogger(RemoveFields.class);

    @Test
    public void removeFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Removing 2 contract categories...");
        Selenide.executeJavaScript("$('input[value=\"Contracting department\"]')[0].scrollIntoView({})");
        contractFields.clickEditValues("Contract category");
        contractFields.removeValue("Contract category", "Amendment - Testcat");
        contractFields.removeValue("Contract category", "Testcat");

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
}
