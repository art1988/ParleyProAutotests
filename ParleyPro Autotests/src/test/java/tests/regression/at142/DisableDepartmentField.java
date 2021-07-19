package tests.regression.at142;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class DisableDepartmentField
{
    private static Logger logger = Logger.getLogger(DisableDepartmentField.class);

    @Test
    public void disableDepartmentField() throws InterruptedException
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        logger.info("Removing Contracting department field...");
        contractFields.removeField("Contracting department").clickDelete();
        Thread.sleep(1_000);

        fieldsPage.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        $("input[value='Contracting department']").shouldBe(Condition.hidden);
    }
}
