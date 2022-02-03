package tests.requests.at171;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;

import static com.codeborne.selenide.Selenide.$;


public class AddRequestCustomField
{
    @Test
    public void addRequestCustomField()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "reqField", FieldType.SELECT, false);
        contractFields.addValues("reqField", "val1");
        contractFields.addValues("reqField", "val2");
        fields.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
