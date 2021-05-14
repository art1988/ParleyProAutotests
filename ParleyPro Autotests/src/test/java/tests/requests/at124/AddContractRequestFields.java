package tests.requests.at124;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractRequestFields
{
    @Test
    @Description("This test creates fields for Contract Request that are the same as from Summary.")
    public void addContractRequestFields() throws InterruptedException
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.createNewFiled("Contract Request", "Contracting region", FieldType.SELECT, false);
        contractFields.addValues("Contracting region", "region1");
        contractFields.addValues("Contracting region", "region2");
        contractFields.addValues("Contracting region", "region3");

        contractFields.createNewFiled("Contract Request", "Contracting country", FieldType.SELECT, false);
        contractFields.addValues("Contracting country", "country1");
        contractFields.addValues("Contracting country", "country2");

        contractFields.createNewFiled("Contract Request", "Contract entity", FieldType.SELECT, false);
        contractFields.addValues("Contract entity", "entity1");
        contractFields.addValues("Contract entity", "entity2");

        contractFields.createNewFiled("Contract Request", "Contracting department", FieldType.SELECT, false);
        contractFields.addValues("Contracting department", "department1");
        contractFields.addValues("Contracting department", "department2");

        contractFields.createNewFiled("Contract Request", "Contract category", FieldType.SELECT, false);
        contractFields.addValues("Contract category", "category1");
        contractFields.addValues("Contract category", "category2");

        contractFields.createNewFiled("Contract Request", "Contract type", FieldType.MULTI_SELECT, false);
        contractFields.addValues("Contract type", "type1");
        contractFields.addValues("Contract type", "type2");
        contractFields.addValues("Contract type", "type3");

        contractFields.createNewFiled("Contract Request", "Contract value", FieldType.CURRENCY, false);

        fieldsPage.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();
    }
}
