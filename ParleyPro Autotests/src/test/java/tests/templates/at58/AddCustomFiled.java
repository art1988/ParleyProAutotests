package tests.templates.at58;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import org.testng.annotations.Test;
import pages.administration.Fields;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selenide.$;


public class AddCustomFiled
{
    @Test
    public void addCustomFiled()
    {
        Fields fieldTab = new SideBar().clickAdministration().clickFieldsTab();

        fieldTab.clickContractFields().createNewFiled("Summary", "Effective Date", FieldType.DATE, false);
        fieldTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
