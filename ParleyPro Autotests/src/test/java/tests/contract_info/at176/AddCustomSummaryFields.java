package tests.contract_info.at176;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddCustomSummaryFields
{
    private static Logger logger = Logger.getLogger(AddCustomSummaryFields.class);

    @Test
    public void addCustomSummaryFields()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        logger.info("Adding of Summary custom fields with all kind of types...");
        contractFields.createNewFiled("Summary", "CheckboxFld",    FieldType.CHECKBOX, false);
        contractFields.createNewFiled("Summary", "DateFld",        FieldType.DATE, false);

        contractFields.createNewFiled("Summary", "MultiSelectFld", FieldType.MULTI_SELECT, false);
        contractFields.addValues("MultiSelectFld", "MS1");
        contractFields.addValues("MultiSelectFld", "MS2");
        contractFields.addValues("MultiSelectFld", "MS3");

        contractFields.createNewFiled("Summary", "NumericFld",     FieldType.NUMERIC, false);
        contractFields.createNewFiled("Summary", "DecimalFld",     FieldType.DECIMAL, false);

        contractFields.createNewFiled("Summary", "RadioFld",       FieldType.RADIO_BUTTON, false);
        contractFields.addValues("RadioFld", "R1");
        contractFields.addValues("RadioFld", "R2");
        contractFields.addValues("RadioFld", "R3");

        contractFields.createNewFiled("Summary", "OnlySelectFld",      FieldType.SELECT, false);
        contractFields.addValues("OnlySelectFld", "S1");
        contractFields.addValues("OnlySelectFld", "S2");
        contractFields.addValues("OnlySelectFld", "S3");

        contractFields.createNewFiled("Summary", "TextFld",        FieldType.TEXT, false);
        contractFields.createNewFiled("Summary", "TextAreaFld",    FieldType.TEXT_AREA, false);

        fields.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
