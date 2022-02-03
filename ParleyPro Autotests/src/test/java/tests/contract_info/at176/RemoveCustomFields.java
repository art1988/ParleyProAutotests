package tests.contract_info.at176;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveCustomFields
{
    @Test
    public void removeCustomFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("TextAreaFld").clickDelete();
        contractFields.removeField("TextFld").clickDelete();
        contractFields.removeField("OnlySelectFld").clickDelete();
        contractFields.removeField("RadioFld").clickDelete();
        contractFields.removeField("DecimalFld").clickDelete();
        contractFields.removeField("NumericFld").clickDelete();
        contractFields.removeField("MultiSelectFld").clickDelete();
        contractFields.removeField("DateFld").clickDelete();
        contractFields.removeField("CheckboxFld").clickDelete();

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 17_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
